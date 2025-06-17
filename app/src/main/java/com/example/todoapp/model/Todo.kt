package com.example.todoapp.model

data class Todo(
    var id:Int? = null,
    var title: String?,
    var description: List<ContentItem>?,
    var completed: Boolean? = false,
    var urlImage: List<String>? = emptyList()
)