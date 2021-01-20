package com.example.e_books.model

data class Books(
    val book_id: Int,
    val name: String,
    val author: String,
    val description: String,
    val bookUrl: String,
    val imageUrl: String,
    val pageNumbers: String,
)