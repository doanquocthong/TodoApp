package com.example.todoapp.uiapp

import android.Manifest
import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.material3.TextButton
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
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.todoapp.R
import com.example.todoapp.model.Todo
import com.example.todoapp.viewmodel.TodoViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AddTodo(navController: NavController, todoViewModel: TodoViewModel) {

    var title by remember { mutableStateOf(TextFieldValue("")) }
    var description by remember { mutableStateOf(TextFieldValue("")) }

    val context = LocalContext.current

    //Chứa uri ảnh được chọn
    var imageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }
//   Mở thư viện chọn ảnh (GetMultipleContents)
    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
            imageUris = imageUris + uris
            Log.d("AddRoomScreen", "Đã chọn ảnh: $uris")
        }
    //Yêu cầu quyền truy cập
    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) galleryLauncher.launch("image/*")
            else Toast.makeText(context, "Quyền truy cập bị từ chối", Toast.LENGTH_SHORT).show()
        }
    // Mở camera và nhận ảnh chụp
    val cameraImageUri = remember { mutableStateOf<Uri?>(null) }
    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            cameraImageUri.value?.let { uri ->
                if (success) {
                    imageUris = imageUris + uri
                    Log.d("AddRoomScreen", "Đã chụp ảnh: $uri")
                }
            }
        }

    var showDialog by remember { mutableStateOf(false) }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Chọn ảnh") },
            text = {
                Column {
                    Text(
                        text = "📷 Chụp ảnh",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val uri = FileProvider.getUriForFile(
                                    context,
                                    "${context.packageName}.provider",
                                    File(
                                        context.cacheDir,
                                        "camera_image_${System.currentTimeMillis()}.jpg"
                                    )
                                )
                                cameraImageUri.value = uri
                                showDialog = false
                                cameraLauncher.launch(uri)
                            }
                            .padding(8.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "🖼 Chọn từ thư viện",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val permission =
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                                        Manifest.permission.READ_MEDIA_IMAGES
                                    else
                                        Manifest.permission.READ_EXTERNAL_STORAGE

                                if (ContextCompat.checkSelfPermission(context, permission)
                                    == PackageManager.PERMISSION_GRANTED
                                ) {
                                    showDialog = false
                                    galleryLauncher.launch("image/*")
                                } else {
                                    showDialog = false
                                    permissionLauncher.launch(permission)
                                }
                            }
                            .padding(8.dp)
                    )
                }
            },
            confirmButton = {

            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Hủy")
                }
            }
        )
    }

    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("THÊM", fontSize = 30.sp, fontWeight = FontWeight.Bold) },
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
                    onTitleChange = {title = it},
                    description = description,
                    onDescriptionChange = {description = it},
                    onAddImageClick = { showDialog = true },
                    imageUris = imageUris,
                    context = context,
                    onRemoveImage = {imageUris -= it},
                )
            }
            item{
                Spacer(
                    Modifier.height(20.dp)
                )
            }
            val todo = Todo(
                title = title.text,
                description = description.text
            )
            item{
                ButtonAddTodo(
                    imageUris = imageUris,
                    navController = navController,
                    todoViewModel = todoViewModel,
                    todo = todo,
                )
            }

        }

    }
}

@Composable
fun TextFieldContainer(
    context: Context,
    title: TextFieldValue,
    onTitleChange: (TextFieldValue) -> Unit,
    description: TextFieldValue,
    onDescriptionChange: (TextFieldValue) -> Unit,
    onAddImageClick: () -> Unit,
    imageUris: List<Uri>,
    onRemoveImage: (Uri) -> Unit,
) {
    var selectedImage by remember { mutableStateOf<Uri?>(null) }

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
                onValueChange = {
                    //title = it
                    onTitleChange(it)
                                },
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
            OutlinedTextField(
                value = description,
                onValueChange = {
                    onDescriptionChange(it)
                },
                modifier = Modifier,
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
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .height(300.dp),
                contentPadding = PaddingValues(0.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp,),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(imageUris) { uri ->
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { selectedImage = if (selectedImage == uri) null else uri },
                        contentAlignment = Alignment.Center,
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(context).data(uri).crossfade(true).build(),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        if (selectedImage == uri) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Black.copy(alpha = 0.4f)),
                                contentAlignment = Alignment.Center
                            ) {
                                IconButton(
                                    onClick = {
                                        onRemoveImage(uri)
                                        selectedImage = null
                                    },
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(Color.White, CircleShape)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete",
                                        tint = colorResource(R.color.main)
                                    )
                                }
                            }
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
fun ButtonAddTodo(
    imageUris: List<Uri> ,
    navController: NavController ,
    todoViewModel: TodoViewModel,
    todo: Todo,
) {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(true) }
    OutlinedButton(
        enabled = isLoading,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 30.dp),
        onClick = {
            isLoading = false
            if (todo.title.isNullOrBlank() || todo.description.isNullOrBlank()) {
                Toast.makeText(context, "Sao không điền cho hết z bà nội", Toast.LENGTH_SHORT).show()
            }

            else {
                todoViewModel.uploadImagesToCloudinary(
                    context = context,
                    imageUris = imageUris,
                    onComplete = { imageUrls ->
                        if (imageUrls.isEmpty()) {
                            return@uploadImagesToCloudinary
                        }
                        todo.urlImages = imageUrls
                        todoViewModel.createTodo(todo)
                        navController.navigate("home")
                        isLoading = true
                    }
                )


                // Sau đó mới gọi lưu
            }
            isLoading = true


        },
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(R.color.main)
        )
    ) {
        if (
            isLoading
        ) {
            Text(
                "Thêm luôn",
                fontSize = 20.sp,
                modifier = Modifier.padding(5.dp)
            )
        }
        else {
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }

    }
}

