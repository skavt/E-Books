package com.example.e_books.services

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.e_books.model.Books
import com.example.e_books.model.Category
import com.example.e_books.model.FavoriteBooks

class BookLiveData : ViewModel() {
    private val _booksLiveData = MutableLiveData<List<Books>>()
    val booksLiveData: LiveData<List<Books>>
        get() = _booksLiveData

    private val _bookData = MutableLiveData<Books>()
    val bookData: LiveData<Books>
        get() = _bookData

    private val _categoryData = MutableLiveData<Category>()
    val categoryData: LiveData<Category>
        get() = _categoryData

    private val _favoriteData = MutableLiveData<FavoriteBooks>()
    val favoriteData: LiveData<FavoriteBooks>
        get() = _favoriteData

    fun setBook(book: Books?) {
        _bookData.postValue(book)
    }

    fun setCategory(category: Category?) {
        _categoryData.postValue(category)
    }

    fun setFavoriteBook(book: FavoriteBooks?) {
        _favoriteData.postValue(book)
    }
}