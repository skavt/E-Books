package com.example.e_books.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_books.R
import com.example.e_books.adapters.CategoryAdapter
import com.example.e_books.adapters.SearchAdapter
import com.example.e_books.extentions.castBookData
import com.example.e_books.model.Books
import com.example.e_books.model.Category
import com.example.e_books.services.BookLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.forEach

class SearchFragment : Fragment(R.layout.search_fragment),
    CategoryAdapter.OnItemClickListener {

    private lateinit var searchItem: RecyclerView
    private lateinit var search: EditText
    private lateinit var searchView: View
    private var searchBookList = ArrayList<Books>()
    private lateinit var db: FirebaseDatabase
    private lateinit var content: LinearLayout
    private lateinit var progressBar: ProgressBar
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
        progressBar = searchView.findViewById(R.id.search_progress_bar)
        content = searchView.findViewById(R.id.search_content)

        search.requestFocus()

        if (bookLiveData.booksLiveData.value != null) {
            searchBookList = bookLiveData.booksLiveData.value as ArrayList<Books>
            setAdapter()
        } else {
            db = Firebase.database

            db.reference.child("categories")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        dataSnapshot.children.forEach {

                            var index = 0
                            val data = it.child("books").value as ArrayList<*>

                            while (index < data.size) {
                                searchBookList.add(castBookData(data[index] as HashMap<*, *>))
                                index++
                            }

                        }
                        setAdapter()

                        bookLiveData.setBooks(searchBookList)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
        }

        return searchView
    }

    override fun onSeeMoreClick(category: Category) {
        TODO("Not yet implemented")
    }

    override fun onBookClick(book: Books) {
        bookLiveData.setBook(book)
        searchView.findNavController().navigate(R.id.book_details_fragment)
    }

    private fun setAdapter() {
        searchItem.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.VERTICAL, false
        )
        searchItem.adapter = SearchAdapter(
            searchBookList, this
        )

        progressBar.visibility = View.GONE
        content.visibility = View.VISIBLE
    }
}