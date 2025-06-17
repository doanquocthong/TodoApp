package com.example.todoapp.uiapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.example.todoapp.R
import com.example.todoapp.model.ContentItem
import com.example.todoapp.model.Todo
import com.example.todoapp.viewmodel.EditorViewModel
import com.example.todoapp.viewmodel.TodoViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AddTodo(navController: NavController, todoViewModel: TodoViewModel, editorViewModel: EditorViewModel) {

    var title by remember { mutableStateOf(TextFieldValue("")) }
    var description by remember { mutableStateOf(TextFieldValue("")) }

    val context = LocalContext.current

    //Chứa uri ảnh được chọn
    var imageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }
//   Mở thư viện chọn ảnh (GetMultipleContents)
    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
        imageUris = imageUris + uris
        Log.d("AddRoomScreen", "Đã chọn ảnh: $uris")
    }
    //Yêu cầu quyền truy cập
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) galleryLauncher.launch("image/*")
        else Toast.makeText(context, "Quyền truy cập bị từ chối", Toast.LENGTH_SHORT).show()
    }
    // Mở camera và nhận ảnh chụp
    val cameraImageUri = remember { mutableStateOf<Uri?>(null) }
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        cameraImageUri.value?.let { uri ->
            if (success) {
                imageUris = imageUris + uri
                Log.d("AddRoomScreen", "Đã chụp ảnh: $uri")
            }
        }
    }

    val openOptionDialog: () -> Unit = {
        AlertDialog.Builder(context)
            .setTitle("Chọn ảnh")
            .setItems(arrayOf("Chụp ảnh", "Chọn từ thư viện")) { _, which ->
                if (which == 0) {
                    // Chụp ảnh
                    val uri = FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.provider", // Khai báo provider trong manifest
                        File(context.cacheDir, "camera_image_${System.currentTimeMillis()}.jpg")
                    )
                    cameraImageUri.value = uri
                    cameraLauncher.launch(uri)
                } else {
                    // Mở thư viện
                    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                        Manifest.permission.READ_MEDIA_IMAGES
                    else
                        Manifest.permission.READ_EXTERNAL_STORAGE

                    if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                        galleryLauncher.launch("image/*")
                    } else {
                        permissionLauncher.launch(permission)
                    }
                }
            }
            .show()
    }


    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("ADD TODO", fontSize = 30.sp, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(R.color.main),
                    titleContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        },
                        colors = IconButtonDefaults.iconButtonColors(
                        ),
                        modifier = Modifier.size(50.dp),
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_arrow_left_24),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(60.dp)
                        )
                    }
                }
            )
        },
    ) { paddingValue ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValue)
                .padding(start = 30.dp, end = 30.dp, top = 20.dp)
//                .verticalScroll(scrollState),*****************

        ) {
            item{
                TextFieldContainer(
                    title = title,
                    onTitleChange = { title = it },
                    description = description,
                    onDescriptionChange = { description = it },
                    onAddImageClick = openOptionDialog,
                    imageUris = imageUris,
                    context = context,
                    onRemoveImage = { uri ->
                        imageUris = imageUris - uri
                    },
                    editorViewModel = editorViewModel
                )
            }
            item{
                Spacer(
                    Modifier.height(20.dp)
                )
            }

            val todo = Todo(
                title = title.text,
                description = description.text,
            )
            item{
                ButtonAddTodo(
                    imageUris = TODO(),
                    navController = TODO(),
                    todoViewModel = TODO(),
                    todo = TODO(),
                    context = TODO()
                )
            }

        }

    }
}

@Composable
fun TextFieldContainer(
    title: TextFieldValue,
    onTitleChange: (TextFieldValue) -> Unit,
    description: List<ContentItem>,
    onDescriptionChange: (TextFieldValue) -> Unit,
    onAddImageClick: () -> Unit,
    imageUris: List<Uri>,
    context: Context,
    onRemoveImage: (Uri) -> Unit,
    editorViewModel: EditorViewModel
) {
    var selectedImage by remember { mutableStateOf<Uri?>(null) }

    //To write texts or add images
    val items = editorViewModel.contentItems
    var isWriting by remember { mutableStateOf(true)}

    Surface(
        border = BorderStroke(1.dp, Color.Gray),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
        ) {
            Text(
                "Tiêu đề",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            OutlinedTextField(
                value = title,
                onValueChange = {onTitleChange(it)},
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White,
                ),
                textStyle = TextStyle(
                    fontSize = 18.sp
                )
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Spacer(
                    Modifier
                        .alpha(0.3f)
                        .height(0.5.dp)
                        .background(color = Color.Gray)
                        .fillMaxWidth(1f),

                    )
            }

            Text(
                "Nội dung",
                modifier = Modifier.padding(top = 20.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
//            OutlinedTextField(
//                value = description,
//                onValueChange = {onDescriptionChange(it)},
//                modifier = Modifier,
//                shape = RoundedCornerShape(16.dp),
//                colors = OutlinedTextFieldDefaults.colors(
//                    focusedContainerColor = Color.White,
//                    unfocusedContainerColor = Color.White,
//                    focusedBorderColor = Color.White,
//                    unfocusedBorderColor = Color.White,
//                ),
//                textStyle = TextStyle(
//                    fontSize = 18.sp
//                )
//            )
//            LazyVerticalGrid(
//                columns = GridCells.Fixed(3),
//                modifier = Modifier
//                    .height(300.dp),
//                contentPadding = PaddingValues(0.dp),
//                verticalArrangement = Arrangement.spacedBy(10.dp,),
//                horizontalArrangement = Arrangement.spacedBy(10.dp)
//            ) {
//                items(imageUris) { uri ->
//                    Box(
//                        modifier = Modifier
//                            .size(100.dp)
//                            .clip(RoundedCornerShape(16.dp))
//                            .clickable { selectedImage = if (selectedImage == uri) null else uri },
//                        contentAlignment = Alignment.Center,
//                    ){
//                        AsyncImage(
//                            model =ImageRequest.Builder(context).data(uri).crossfade(true).build(),
//                            contentDescription = null,
//                            modifier = Modifier.fillMaxSize(),
//                            contentScale = ContentScale.Crop
//                        )
//                        if (selectedImage == uri) {
//                            Box(
//                                modifier = Modifier
//                                    .fillMaxSize()
//                                    .background(Color.Black.copy(alpha = 0.4f)),
//                                contentAlignment = Alignment.Center
//                            ) {
//                                IconButton(
//                                    onClick = {
//                                        onRemoveImage(uri)
//                                        selectedImage = null
//                                    },
//                                    modifier = Modifier
//                                        .size(32.dp)
//                                        .background(Color.White, CircleShape)
//                                ) {
//                                    Icon(
//                                        imageVector = Icons.Default.Delete,
//                                        contentDescription = "Delete",
//                                        tint = colorResource(R.color.main)
//                                    )
//                                }
//                            }
//                        }
//                    }
//
//                }
//            }
            LazyColumn {
                itemsIndexed(items) { index, item ->
                    when (item) {
                        is ContentItem.TextItem -> {
                            OutlinedTextField(
                                value = item.text,
                                onValueChange = { editorViewModel.updateText(index, it) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        is ContentItem.ImageItem -> {
                            AsyncImage(
                                model = item.uri,
                                contentDescription = null,
                                modifier = Modifier
                                    .height(200.dp)
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                            )
                        }
                    }
                }

            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                FloatingActionButton(
                    onClick = {
                        onAddImageClick()
                    },
                    containerColor = colorResource(R.color.background),
                    elevation = FloatingActionButtonDefaults.elevation(
                    ),
                    modifier = Modifier
                        .size(60.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.image_gallery),
                        contentDescription = null,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ButtonAddTodo(imageUris: List<Uri> ,navController: NavController ,todoViewModel: TodoViewModel, todo: Todo, context: Context) {
    OutlinedButton(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 30.dp),
        onClick = {
            todoViewModel.uploadImagesToCloudinary(
                context = context,
                imageUris = imageUris,
                onComplete = { imageUrls ->
                        if (imageUrls.isEmpty()) {
                            Toast.makeText(context, "Không có ảnh nào được tải lên", Toast.LENGTH_SHORT).show()
                            return@uploadImagesToCloudinary
                        }
                        todoViewModel.createTodo(todo)
                }
            )
            todoViewModel.createTodo(todo)
            navController.navigate("home")
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(R.color.main)
        )
    ) {
        Text(
            "Thêm công việc",
            fontSize = 20.sp,
            modifier = Modifier.padding(5.dp)
        )
    }
}

