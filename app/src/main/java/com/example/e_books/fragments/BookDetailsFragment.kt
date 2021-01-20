package com.example.e_books.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import com.example.e_books.R
import com.example.e_books.services.BookLiveData

class BookDetailsFragment : Fragment(R.layout.book_details_fragment) {
    private lateinit var bookDetailsView: View
    private val bookLiveData: BookLiveData by navGraphViewModels(R.id.books_nav)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bookDetailsView = inflater.inflate(R.layout.book_details_fragment, container, false)
        setHasOptionsMenu(true)

        bookLiveData.bookData.observe(viewLifecycleOwner, { book ->
            with(book) {
                (activity as AppCompatActivity).title = name

            }
        })

        return bookDetailsView
    }
}