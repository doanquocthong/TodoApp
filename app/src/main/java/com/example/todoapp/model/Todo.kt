package com.example.todoapp.model

data class Todo(
    var id: Int? = null,
    var title: String? = "",
    var description: String? = "",
    var completed: Boolean? = false,
    var urlImages: List<String>? = emptyList()
)
//
//sealed class ContentItem {
//    data class TextItem(var text: String) : ContentItem()
//    data class ImageItem(val uri: Uri) : ContentItem()
//}