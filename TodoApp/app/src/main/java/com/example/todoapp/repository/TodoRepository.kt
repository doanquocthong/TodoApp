package com.example.todoapp.repository

import com.example.todoapp.model.Todo
import com.example.todoapp.service.ApiService
import com.example.todoapp.service.RetrofitInstance

class TodoRepository(){
    suspend fun getAllTodos():List<Todo> {
        return RetrofitInstance.api.getAll()
    }

    suspend fun getTodoById(id: Int):Todo {
        return RetrofitInstance.api.getById(id)
    }

    suspend fun createTodo(todo: Todo): Todo {
        return RetrofitInstance.api.create(todo)
    }

    suspend fun updateTodo(id: Int, todo: Todo):Todo{
        return RetrofitInstance.api.update(id, todo )
    }

    suspend fun deleteTodo(id: Int){
        RetrofitInstance.api.delete(id)
    }
    suspend fun deleteAllTodo(){
        RetrofitInstance.api.deleteAll()
    }

}