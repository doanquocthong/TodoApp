package com.example.todoapp.uiapp

import android.net.Uri
import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.todoapp.R
import com.example.todoapp.viewmodel.TodoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Detail(navController:NavController, id: Int, todoViewModel: TodoViewModel){
    var selectedImage by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    LaunchedEffect(key1 = id) {
        todoViewModel.getTodoById(id)
    }
    val selectedTodo = todoViewModel.selectedTodo
    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("CHI TIẾT", fontSize = 30.sp, fontWeight = FontWeight.Bold) },
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
        floatingActionButton = {
            Column {
                FloatingActionButton(
                    onClick = {

                    },
                    modifier = Modifier
                        .size(70.dp).alpha(alpha = 0.2f),
                    shape = CircleShape,
                    containerColor = Color.White,
                ) {
                    Image(
                        painter = painterResource(R.drawable.diskette),
                        contentDescription = null,
                        modifier = Modifier.size(30.dp),
                    )
                }
                Spacer(
                    Modifier.height(20.dp)
                )
                FloatingActionButton(
                    onClick = {
                        todoViewModel.deleteTodoById(id)
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .size(70.dp),
                    shape = CircleShape,
                    containerColor = Color.White,
                ) {
                    Image(
                        painter = painterResource(R.drawable.delete),
                        contentDescription = null,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

        }
    ){ paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(30.dp)
        ) {
            selectedTodo.value?.title?.let {
                Text(
                    it,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            selectedTodo.value?.description?.let {
                Text(
                    it,
                    fontSize = 20.sp
                )
            }
            selectedTodo.value?.urlImages?.let { urlImages ->
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .height(300.dp),
                    contentPadding = PaddingValues(0.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp,),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(urlImages) { urlImage ->
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .clickable {
//                                    selectedImage = if (selectedImage == uri) null else uri
                                },
                            contentAlignment = Alignment.Center,
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(context).data(urlImage).crossfade(true)
                                    .build(),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
//                            if (selectedImage == uri) {
//                                Box(
//                                    modifier = Modifier
//                                        .fillMaxSize()
//                                        .background(Color.Black.copy(alpha = 0.4f)),
//                                    contentAlignment = Alignment.Center
//                                ) {
//                                    IconButton(
//                                        onClick = {
//                                            onRemoveImage(uri)
//                                            selectedImage = null
//                                        },
//                                        modifier = Modifier
//                                            .size(32.dp)
//                                            .background(Color.White, CircleShape)
//                                    ) {
//                                        Icon(
//                                            imageVector = Icons.Default.Delete,
//                                            contentDescription = "Delete",
//                                            tint = colorResource(R.color.main)
//                                        )
//                                    }
//                                }
//                            }
                        }
                    }
                }
            }
        }
    }
}
