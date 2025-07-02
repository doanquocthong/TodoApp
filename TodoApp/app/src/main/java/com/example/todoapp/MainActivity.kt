package com.example.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.todoapp.uiapp.AddTodo
import com.example.todoapp.uiapp.Detail
import com.example.todoapp.uiapp.TodoApp
import com.example.todoapp.viewmodel.TodoViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val todoViewModel: TodoViewModel = viewModel()
            val navController = rememberNavController()
            NavHost(navController, startDestination = "home") {
                composable("add") {
                    AddTodo(
                        navController = navController,
                        todoViewModel = todoViewModel,
                    )
                }
                composable("home") {
                    TodoApp(
                        navController = navController,
                        todoViewModel = todoViewModel,
                    )
                }
                composable(
                    "detail/{id}",
                    arguments = listOf(navArgument("id") { type = NavType.IntType })
                ) { backStackEntry ->
                    val todoId = backStackEntry.arguments?.getInt("id") ?: -1
                    Detail(
                        navController,
                        id = todoId,
                        todoViewModel = todoViewModel,
                    )
                }

            }
        }
    }
}
