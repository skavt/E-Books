package com.example.e_books.extentions

import com.example.e_books.model.Books
import com.example.e_books.model.Category

fun castCategoryData(categoryData: java.util.HashMap<*, *>): Category {
    val id = categoryData["id"].toString().toInt()
    val categoryName = categoryData["category_name"].toString()
    val books = categoryData["books"] as ArrayList<*>

    var index = 0
    val bookList = ArrayList<Books>()

    while (index < books.size) {
        bookList.add(castBookData(books[index] as HashMap<*, *>))
        index++
    }
    return Category(id, categoryName, bookList)
}

fun castBookData(bookData: HashMap<*, *>): Books {

    val bookId = bookData["book_id"].toString().toInt()
    val name = bookData["name"].toString()
    val author = bookData["author"].toString()
    val description = bookData["description"].toString()
    val bookUrl = bookData["bookUrl"].toString()
    val imageUrl = bookData["imageUrl"].toString()
    val pageNumbers = bookData["pageNumbers"].toString()

    return Books(bookId, name, author, description, bookUrl, imageUrl, pageNumbers)
}