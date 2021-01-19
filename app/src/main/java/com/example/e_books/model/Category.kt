package com.example.e_books.model

data class Category(
    val id: Int,
    val books: List<Books>,
    val category_name: String
)