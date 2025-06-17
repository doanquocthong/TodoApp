package com.example.todoapp.viewmodel

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.transform.Transformation
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.example.todoapp.model.Todo
import com.example.todoapp.repository.TodoRepository
import com.example.todoapp.service.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
class TodoViewModel:ViewModel() {
    private val todoRepository = TodoRepository()

    //get todo list
    private val _todoList = mutableStateOf<List<Todo>>(emptyList())
    val todoList: State<List<Todo>> = _todoList

//    Get todo by id
    private val _selectedTodo = mutableStateOf<Todo?>(null)
    val selectedTodo: State<Todo?> = _selectedTodo

    init {
        getAllTodo()
    }
    private fun getAllTodo() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _todoList.value = todoRepository.getAllTodos()
            }
            catch (
                e:Exception
            ) {
                Log.e("API TODO", "Get todo fail: ${e.localizedMessage}", e)
            }
        }
    }
    private fun getTodoById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val todo = todoRepository.getTodoById(id)
                _selectedTodo.value = todo
            }
            catch (
                e:Exception
            ) {
                Log.e("API TODO", "Get todo by ID fail: ${e.localizedMessage}", e)
            }
        }
    }
    fun createTodo(todo: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val createTodo = todoRepository.createTodo(todo)
                _todoList.value += createTodo
            }
            catch (
                e:Exception
            ) {
                Log.e("API TODO", "Create todo fail: ${e.localizedMessage}", e)
            }
        }
    }
    fun updateTodo(id: Int ,todo: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val updateTodo = todoRepository.updateTodo(id, todo)
                _todoList.value = _todoList.value.map {
                    if (it.id == updateTodo.id)
                        updateTodo
                    else it
                }
            }
            catch (
                e:Exception
            ) {
                Log.e("API TODO", "Update todo fail: ${e.localizedMessage}", e)
            }
        }
    }
    fun deleteTodoById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val findById = todoRepository.deleteTodo(id)
                _todoList.value = _todoList.value.filter { it.id != id }
            }
            catch (
                e:Exception
            ) {
                Log.e("API TODO", "Delete todo fail: ${e.localizedMessage}", e)
            }
        }
    }
    fun deleteAllTodo() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _todoList.value = emptyList()
            }
            catch (
                e:Exception
            ) {
                Log.e("API TODO", "Delete all todo fail: ${e.localizedMessage}", e)
            }
        }
    }

    fun uploadImagesToCloudinary(
        context: Context,
        //Uri hình ảnh chọn từ album
        imageUris: List<Uri>,
        //Trả về String url sau khi upload xong hết
        onComplete: (List<String>) -> Unit,
//        //Xử lí lỗi
//        onError: (String) -> Unit
    ) {

        val uploadedUrls = mutableListOf<String>()
        //Số lượng ảnh đã chọn
        var uploadCount = 0

        //Trả về list trống khi không chọn ảnh
//        if (imageUris.isEmpty()) {
//            onComplete(emptyList())
//            return
//        }

        //Duyệt qua từng phần từ của list imaggeUris
        imageUris.forEach { uri ->
            //Upload ảnh lên Cloudinary
//          Với mỗi ảnh (uri) upload lên Cloudinary
            MediaManager.get().upload(uri)
                .option("transformation", "c_fill,w_300,h_300")
                //Xử lí phản hồi trong quá trình upload ảnh.
                .callback(object : com.cloudinary.android.callback.UploadCallback {
                    override fun onStart(requestId: String?) {
                        Log.d("CloudinaryUpload", "Bắt đầu tải: $uri")
                    }

                    override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {
                        Log.d("CloudinaryUpload", "Tiến độ: $bytes/$totalBytes cho $uri")
                    }

                    override fun onSuccess(requestId: String?, resultData: Map<*, *>) {
                        val url = resultData["secure_url"] as? String
                        if (url != null) {
                            uploadedUrls.add(url)
                            Log.d("CloudinaryUpload", "Tải thành công: $url")
                        } else {
                            Log.e("CloudinaryUpload", "Không có secure_url trong kết quả: $resultData")
                        }
                        uploadCount++
                        if (uploadCount == imageUris.size) {
                            onComplete(uploadedUrls)
                        }
                    }

                    override fun onError(requestId: String?, error: ErrorInfo?) {
                        Log.e("CloudinaryUpload", "Lỗi tải $uri: ${error?.description}")
                        uploadCount++
                        if (uploadCount == imageUris.size) {
                            onComplete(uploadedUrls)
                        }
//                        onError("Lỗi tải ảnh: ${error?.description ?: "Không xác định"}")
                    }

                    override fun onReschedule(requestId: String?, error: ErrorInfo?) {
                        Log.w("CloudinaryUpload", "Tải bị hoãn $uri: ${error?.description}")
//                        onError("Tải bị hoãn: ${error?.description ?: "Không xác định"}")
                    }
                })
                //Start progress upload (truyền vào context hiện tại
                .dispatch(context)
        }
    }
}