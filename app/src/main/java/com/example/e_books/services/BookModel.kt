package com.example.e_books.services

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.e_books.model.Categories
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookModel : ViewModel() {
    private val _bookLiveData = MutableLiveData<Categories>()
    val bookLiveData: LiveData<Categories>
        get() = _bookLiveData

    fun getBooks() {
        val jsonApi = Api.create().getBooks()

        jsonApi.enqueue(object : Callback<Categories> {
            override fun onResponse(call: Call<Categories>, response: Response<Categories>) {
                response.body()?.let { _bookLiveData.postValue(it) }
            }

            override fun onFailure(call: Call<Categories>, t: Throwable) {
                Log.d("Error", t.message.toString())
            }
        })
    }
}