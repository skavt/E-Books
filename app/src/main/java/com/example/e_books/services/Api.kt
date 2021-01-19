package com.example.e_books.services

import retrofit2.Call
import com.example.e_books.model.Categories
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface Api {
    @GET("movies-db/db")
    fun getBooks(): Call<Categories>

    companion object {

        //TODO Add valid url
        var BASE_URL = "https://my-json-server.typicode.com/nikoloz14/"

        fun create(): Api {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()

            return retrofit.create(Api::class.java)
        }
    }
}