package com.example.e_books.extentions

import com.example.e_books.model.Books
import com.example.e_books.model.Category
import com.example.e_books.model.FavoriteBooks

fun castCategoryData(categoryData: java.util.HashMap<*, *>): Category {
    val id = categoryData["id"].toString().toInt()
    val categoryName = categoryData["category_name"].toString()
    val books = categoryData["books"] as ArrayList<*>

    return Category(id, categoryName, castBookData(books))
}

fun castFavoritesData(favoritesData: ArrayList<*>): ArrayList<FavoriteBooks> {
    val size = favoritesData.size
    var index = 0
    val bookList = ArrayList<FavoriteBooks>()

    while (index < size) {
        val mapBookData: HashMap<*, *> = favoritesData[index] as HashMap<*, *>
//        val bookId = mapBookData["book_id"].toString().toInt()
        val name = mapBookData["name"].toString()
//        val author = mapBookData["author"].toString()
//        val description = mapBookData["description"].toString()
//        val bookUrl = mapBookData["bookUrl"].toString()
//        val imageUrl = mapBookData["imageUrl"].toString()
//        val pageNumbers = mapBookData["pageNumbers"].toString()

        bookList.add(FavoriteBooks(name))
        index++
    }
    return bookList
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