package com.example.e_books.extentions

import com.example.e_books.model.Books
import com.example.e_books.model.Category

fun castCategoryData(categoryData: ArrayList<*>): ArrayList<Category> {
    val size = categoryData.size
    var index = 0
    val categoryList = ArrayList<Category>()

    while (index < size) {
        val mapCategoryData: HashMap<*, *> = categoryData[index] as HashMap<*, *>
        val id = mapCategoryData["id"].toString().toInt()
        val categoryName = mapCategoryData["category_name"].toString()
        val books = mapCategoryData["books"] as ArrayList<*>

        categoryList.add(Category(id, categoryName, castBookData(books)))
        index++
    }
    return categoryList
}

fun castBookData(bookData: ArrayList<*>): ArrayList<Books> {
    val size = bookData.size
    var index = 0
    val bookList = ArrayList<Books>()

    while (index < size) {
        val mapBookData: HashMap<*, *> = bookData[index] as HashMap<*, *>
        val bookId = mapBookData["book_id"].toString().toInt()
        val name = mapBookData["name"].toString()
        val author = mapBookData["author"].toString()
        val description = mapBookData["description"].toString()
        val bookUrl = mapBookData["bookUrl"].toString()
        val imageUrl = mapBookData["imageUrl"].toString()
        val pageNumbers = mapBookData["pageNumbers"].toString()

        bookList.add(Books(bookId, name, author, description, bookUrl, imageUrl, pageNumbers))
        index++
    }
    return bookList
}