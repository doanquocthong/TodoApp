package com.example.todoapp.service

import com.example.todoapp.model.Todo
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService{
    @GET("TodoApp")
    suspend fun getAll():List<Todo>
    @GET("TodoApp/{id}")
    suspend fun getById(@Path("id") id: Int):Todo
    @DELETE("TodoApp/{id}")
    suspend fun delete(@Path("id") id: Int)
    @DELETE("TodoApp")
    suspend fun deleteAll()
    @POST("TodoApp")
    suspend fun create(@Body todo: Todo):Todo
    @PUT("TodoApp/{id}")
    suspend fun update(@Path("id") id: Int,@Body todo: Todo):Todo
}