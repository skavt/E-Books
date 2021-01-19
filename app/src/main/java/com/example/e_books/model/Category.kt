package com.example.e_books.model

data class Category(
    val id: Int,
    val category_name: String,
    val books: List<Books>
)