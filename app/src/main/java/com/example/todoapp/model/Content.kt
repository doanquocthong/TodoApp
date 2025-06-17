package com.example.todoapp.model

import android.net.Uri

sealed class ContentItem {
    data class TextItem(var text: String) : ContentItem()
    data class ImageItem(val uri: Uri) : ContentItem()
}
