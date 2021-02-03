package com.example.todolist

data class TopHeadlines(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
)