package com.example.e_books.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import com.example.e_books.R
import com.example.e_books.services.BookLiveData
import com.google.firebase.auth.FirebaseAuth

class FavoritesFragment : Fragment(R.layout.favorites_fragment) {
    private lateinit var auth: FirebaseAuth
    private lateinit var favoritesView: View
    private val bookLiveData: BookLiveData by navGraphViewModels(R.id.books_nav)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        favoritesView = inflater.inflate(R.layout.favorites_fragment, container, false)
        setHasOptionsMenu(true)

        Log.d("aaaaaaaaa", auth.currentUser.toString())

        return favoritesView
    }
}