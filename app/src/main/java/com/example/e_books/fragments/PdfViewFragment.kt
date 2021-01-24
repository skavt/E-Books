package com.example.e_books.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.navGraphViewModels
import com.example.e_books.R
import com.example.e_books.services.BookLiveData

class PdfViewFragment : Fragment(R.layout.pdf_view_fragment) {

    private lateinit var pdfView: View

    private val bookLiveData: BookLiveData by navGraphViewModels(R.id.books_nav)

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        pdfView = inflater.inflate(R.layout.pdf_view_fragment, container, false)
        setHasOptionsMenu(true)

        bookLiveData.bookData.observe(viewLifecycleOwner, { book ->
            with(book) {
                (activity as AppCompatActivity).title = name

                val webView = pdfView.findViewById(R.id.web_view) as WebView
                webView.settings.javaScriptEnabled = true
                webView.loadUrl(bookUrl)
            }
        })

        return pdfView
    }
}