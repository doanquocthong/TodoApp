//package com.example.todoapp.viewmodel
//import android.content.Context
//import android.content.pm.PackageManager
//import android.net.Uri
//import android.os.Build
//import android.util.Log
//import android.widget.Toast
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.runtime.MutableState
//import androidx.compose.runtime.State
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.core.content.ContextCompat
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import coil.transform.Transformation
//import com.cloudinary.android.MediaManager
//import com.cloudinary.android.callback.ErrorInfo
//import com.example.todoapp.model.ContentItem
//import com.example.todoapp.model.Todo
//import com.example.todoapp.repository.TodoRepository
//import com.example.todoapp.service.RetrofitInstance
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//
//class EditorViewModel : ViewModel() {
//    var contentItems by mutableStateOf<List<ContentItem>>(emptyList())
//        private set
//
//    fun addTextItem() {
//        contentItems = contentItems + ContentItem.TextItem("")
//    }
//
//    fun addImageItem(uri: Uri) {
//        contentItems = contentItems + ContentItem.ImageItem(uri)
//    }
//
//    fun updateText(index: Int, newText: String) {
//        val updated = contentItems.toMutableList()
//        val item = updated[index]
//        if (item is ContentItem.TextItem) {
//            updated[index] = item.copy(text = newText)
//        }
//        contentItems = updated
//    }
//
//    fun removeItem(index: Int) {
//        contentItems = contentItems.toMutableList().apply { removeAt(index) }
//    }
//}
