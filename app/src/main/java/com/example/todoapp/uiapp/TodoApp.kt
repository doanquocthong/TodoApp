package com.example.todoapp.uiapp

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.todoapp.R
import com.example.todoapp.model.Todo
import com.example.todoapp.viewmodel.TodoViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoApp(navController: NavController ,todoViewModel: TodoViewModel) {
    val todo = todoViewModel.todoList

    if (todo.value.isEmpty()) {
        Scaffold (
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("TODO APP", fontSize = 30.sp, fontWeight = FontWeight.Bold)},
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = colorResource(R.color.main),
                        titleContentColor = Color.White
                    )
                )
            },
        ){ paddingValues ->
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Text(
                    "Chờ chút bà nội",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(paddingValues)
                        .size(300.dp),
                    color = colorResource(R.color.main)
                )
            }

        }
    }
    else {
        Scaffold (
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("TODO APP", fontSize = 30.sp, fontWeight = FontWeight.Bold)},
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = colorResource(R.color.main),
                        titleContentColor = Color.White
                    )
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        navController.navigate("add")
                    },
                    modifier = Modifier
                        .size(80.dp),
                    shape = CircleShape,
                    containerColor = Color.White,
                ) {
                    Image(
                        painter = painterResource(R.drawable.add),
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp),
                    )
                }
            }
        ){ paddingValues ->
            LazyColumn (
                modifier = Modifier
                    .padding(paddingValues)
            ){
                items(todo.value.size) { todoItem ->
                    TodoItem(
                        todo.value[todoItem],
                        todoViewModel
                    )
                }
            }
        }
    }
}


@Composable
fun dialogTodo(
    onDismiss: () -> Unit,
    todoViewModel: TodoViewModel)
{
    var title by remember { mutableStateOf<String>("")}
    var description by remember {  mutableStateOf<String>("")}
    var completed by remember { mutableStateOf(false) }

    val context = LocalContext.current
    Dialog(
        onDismissRequest = {

        }
    ) {
        Surface (
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier
                .width(500.dp)
        ) {
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ){
                Text(
                    "Thêm công việc",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )
                OutlinedTextField(
                    label = { Text("Tiêu đề")},
                    value = title,
                    onValueChange = {title = it},
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                )
                Spacer(
                    modifier = Modifier.height(10.dp)
                )
                OutlinedTextField(
                    label = { Text("Mô tả")},
                    value = description,
                    onValueChange = {description = it},
                    placeholder = { Text("Tiêu đề")},
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                )
                Spacer(
                    modifier = Modifier.height(30.dp)
                )
                Row (
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    OutlinedButton(
                        onClick = {
                            //Clear data
                            title = ""
                            description = ""
                            onDismiss()
                        },
                        modifier = Modifier,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red
                        )
                    ) {
                        Text(
                            "Hủy",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(5.dp)
                        )
                    }
                    OutlinedButton(
                        onClick = {
                            if((title == "") || (description == "")) {
                                Toast.makeText(context, "Đùa à? Không được để cái nào trống bạn ơi", Toast.LENGTH_SHORT).show()
                            }
                            else {
                                val newTodo = Todo(
                                    title = title,
                                    description = description,
                                    completed = completed,
                                )
                                todoViewModel.createTodo(newTodo)

                                //Clear data
                                title = ""
                                description = ""
                                onDismiss()
                            }
                        },
                        modifier = Modifier,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.main)
                        )
                    ) {
                        Text(
                            "Thêm",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(5.dp)
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun TodoItem(
    todo: Todo,
    todoViewModel: TodoViewModel
) {
    var checked by remember { mutableStateOf(todo.completed) }

//    val descriptionShort = todo.description?.let { desc ->
//        val hasNewLine = desc.contains("\n")
//        val short = desc.substringBefore("\n")
//        if (hasNewLine || short.length > 15) short.take(15) + "..."
//        else short
//    } ?: ""
    val descriptionShort = todo.description?.let { desc ->
        val hasNewLine = desc.contains("\n")
        when{
            hasNewLine -> desc.substringBefore("\n") + "..."
            desc.length > 15 -> desc.take(15) + "..."
            else -> desc
        }
    }?:" "


    Surface(
        border = BorderStroke(1.dp, color = Color.Gray),
        modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 30.dp),
        shape = RoundedCornerShape(16.dp),
        color = colorResource(R.color.background)
    ) {
        Row (
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                modifier = Modifier
                    .padding(end = 15.dp)
                    .size(32.dp)
                    .clickable { checked = !checked!! },
                shape = CircleShape,
                color = if (checked == true) colorResource(R.color.main) else Color.White,
                tonalElevation = 4.dp,
                border = BorderStroke(1.dp, color = Color.Gray)
            ) {
                // Checkbox nhỏ bên trong hình tròn
                checked.let {
                    if (it != null) {
                        Checkbox(
                            checked = it,
                            onCheckedChange = {
                                checked = it
                                todo.completed = checked
                                todo.id?.let { it1 ->
                                    todoViewModel.updateTodo(
                                        it1,
                                        todo
                                    )
                                }
                                              },
                            colors = CheckboxDefaults.colors(
                                checkedColor = colorResource(R.color.main),
                                uncheckedColor = Color.White,
                                checkmarkColor = Color.White
                            ),
                            modifier = Modifier
                                .padding(4.dp)
                                .size(24.dp)
                        )
                    }
                }
            }
            Column (
                modifier = Modifier
                    .weight(1f)
            ) {
                todo.title?.let {
                    Text(
                        it,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                descriptionShort?.let {
                    Text(
                        it,
                        fontSize = 20.sp,
                    )
                }
            }
            ElevatedButton(
                onClick = {

                },
                shape = CircleShape,
                modifier = Modifier.size(48.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.pen),
                    contentDescription = null,
                    modifier = Modifier
                        .size(30.dp),
                )
            }

        }
    }
}
