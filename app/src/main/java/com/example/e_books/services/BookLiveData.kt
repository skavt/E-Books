package com.example.e_books.services

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.e_books.model.Books
import com.example.e_books.model.Category

class BookLiveData : ViewModel() {
    private val _booksLiveData = MutableLiveData<List<Books>>()
    val booksLiveData: LiveData<List<Books>>
        get() = _booksLiveData

    private val _favBooksLiveData = MutableLiveData<List<Books>>()
    val favBooksLiveData: LiveData<List<Books>>
        get() = _favBooksLiveData

    private val _bookData = MutableLiveData<Books>()
    val bookData: LiveData<Books>
        get() = _bookData

    private val _categoryData = MutableLiveData<Category>()
    val categoryData: LiveData<Category>
        get() = _categoryData

    fun setBook(book: Books?) {
        _bookData.postValue(book)
    }

    fun setCategory(category: Category?) {
        _categoryData.postValue(category)
    }

    fun setBooks(books: List<Books>?) {
        _booksLiveData.postValue(books)
    }

    fun setFavBooks(books: List<Books>?) {
        _favBooksLiveData.postValue(books)
    }
}