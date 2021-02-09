package com.example.todolist

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class Article(
    val source: Source,
    val author: String?,
    val title: String?,
    val description: String?,
    val url: String?,
    val urlToImage: String?,
    val publishedAt: String?,
    val content: String?
) : Serializable