package com.example.todoapp

import android.app.Application
import android.util.Log
import com.cloudinary.android.MediaManager

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        try {
            MediaManager.init(this, hashMapOf(
                "cloud_name" to "dtnjhu4v2",
                "api_key" to "122697332814498",
                "api_secret" to "sIp9q2r5GR8Sgy0__wlMeLQkrik"
            ))
            Log.d("Cloudinary", "Khởi tạo Cloudinary thành công")
        } catch (e: Exception) {
            Log.e("Cloudinary", "Lỗi khởi tạo Cloudinary: ${e.message}", e)
        }
    }
}