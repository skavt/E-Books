package com.example.e_books.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

class SearchFragment : Fragment(R.layout.search_fragment),
    CategoryAdapter.OnItemClickListener {

    private var searchBookList = ArrayList<Books>()
    private var listOfBooks = ArrayList<Books>()

    private lateinit var searchView: View
    private lateinit var noData: ImageView
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var content: LinearLayout
    private lateinit var searchInput: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var searchItem: RecyclerView

    private val bookLiveData: BookLiveData by navGraphViewModels(R.id.books_nav)

    @SuppressLint("ClickableViewAccessibility")
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

        auth = Firebase.auth
        db = Firebase.database
        noData = searchView.findViewById(R.id.no_data)
        searchInput = searchView.findViewById(R.id.search)
        searchItem = searchView.findViewById(R.id.search_item)
        content = searchView.findViewById(R.id.search_content)
        progressBar = searchView.findViewById(R.id.search_progress_bar)

        searchInput.requestFocus()
        listOfBooks.clear()

        val booksData = bookLiveData.booksLiveData.value

        when (auth.currentUser) {
            null -> findNavController().navigate(R.id.action_search_to_login)
            else -> {
                when {
                    booksData != null -> {
                        searchBookList = booksData as ArrayList<Books>
                        setAdapter()
                    }
                    else -> {
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
                                    Toast.makeText(
                                        context,
                                        getString(R.string.something_wrong),
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            })
                    }
                }

                searchInput.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(p0: Editable?) {}

                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        listOfBooks.clear()
                        listOfBooks.addAll(searchBookList.search(p0.toString()))
                        (searchItem.adapter as SearchAdapter).notifyDataSetChanged()
                        when {
                            listOfBooks.isEmpty() -> {
                                noData.visibility = VISIBLE
                                searchItem.visibility = GONE
                            }
                            else -> {
                                searchItem.visibility = VISIBLE
                                noData.visibility = GONE
                            }
                        }
                    }

                })

                searchInput.setOnTouchListener(OnTouchListener { _, event ->
                    if (event.action == MotionEvent.ACTION_UP) {
                        if (event.rawX + 30 >= searchInput.right - searchInput.compoundDrawables[2].bounds.width()
                        ) {
                            searchInput.text.clear()
                            return@OnTouchListener true
                        }
                    }
                    false
                })
            }
        }
        return searchView
    }

    fun List<Books>.search(nameChars: String): List<Books> {
        val list = ArrayList<Books>()
        forEach {
            when {
                it.name.toLowerCase(Locale.ROOT)
                    .contains(nameChars.toLowerCase(Locale.ROOT)) -> list.add(it)
                it.author.toLowerCase(Locale.ROOT)
                    .contains(nameChars.toLowerCase(Locale.ROOT)) -> list.add(it)
            }
        }
        return list
    }

    override fun onSeeMoreClick(category: Category) {}

    override fun onBookClick(book: Books) {
        bookLiveData.setBook(book)
        searchView.findNavController().navigate(R.id.action_search_to_book_details)
    }

    private fun setAdapter() {
        searchItem.apply {
            layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.VERTICAL, false
            )
            adapter = SearchAdapter(listOfBooks, this@SearchFragment)
        }

        listOfBooks.addAll(searchBookList)
        (searchItem.adapter as SearchAdapter).notifyDataSetChanged()

        progressBar.visibility = View.GONE
        content.visibility = View.VISIBLE
    }
}