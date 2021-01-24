package com.example.e_books.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_books.R
import com.example.e_books.adapters.CategoryAdapter
import com.example.e_books.adapters.SearchAdapter
import com.example.e_books.model.Books
import com.example.e_books.model.Category
import com.example.e_books.services.BookLiveData

class SearchFragment : Fragment(R.layout.search_fragment),
    CategoryAdapter.OnItemClickListener {

    private lateinit var searchItem: RecyclerView
    private lateinit var search: EditText
    private lateinit var searchView: View
    private val bookLiveData: BookLiveData by navGraphViewModels(R.id.books_nav)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        searchView = inflater.inflate(R.layout.search_fragment, container, false)
        (activity as AppCompatActivity).apply {
            title = getString(R.string._search)
            supportActionBar?.apply {
                show()
                setDisplayHomeAsUpEnabled(false)
            }
        }
        searchItem = searchView.findViewById(R.id.search_item)
        search = searchView.findViewById(R.id.search)

        search.requestFocus()

        searchItem.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.VERTICAL, false
        )
        searchItem.adapter = SearchAdapter(
            bookLiveData.booksLiveData.value as ArrayList<Books>, this
        )

        return searchView
    }

    override fun onSeeMoreClick(category: Category) {
        TODO("Not yet implemented")
    }

    override fun onBookClick(book: Books) {
        bookLiveData.setBook(book)
        searchView.findNavController().navigate(R.id.book_details_fragment)
    }
}