package com.example.e_books.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_books.R
import com.example.e_books.adapters.CategoryAdapter
import com.example.e_books.extentions.castBookData
import com.example.e_books.extentions.castCategoryData
import com.example.e_books.model.Books
import com.example.e_books.model.Category
import com.example.e_books.services.BookLiveData
import com.google.android.material.bottomnavigation.BottomNavigationView
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


class CategoryFragment : Fragment(R.layout.category_fragment), CategoryAdapter.OnItemClickListener {

    private val favBookList = ArrayList<Books>()
    private var categoryList = ArrayList<Category>()

    private lateinit var auth: FirebaseAuth
    private lateinit var categoryView: View
    private lateinit var db: FirebaseDatabase
    private lateinit var progressBar: ProgressBar
    private lateinit var content: NestedScrollView
    private lateinit var categoryItem: RecyclerView
    private lateinit var bottomNav: BottomNavigationView

    private val bookLiveData: BookLiveData by navGraphViewModels(R.id.books_nav)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        categoryView = inflater.inflate(R.layout.category_fragment, container, false)

        auth = Firebase.auth
        when (auth.currentUser) {
            null -> findNavController().navigate(R.id.action_category_to_login)
            else -> {
                (activity as AppCompatActivity).apply {
                    supportActionBar?.show()
                    title = getString(R.string.app_name)
                    bottomNav = findViewById(R.id.bottom_navigation)
                    bottomNav.apply {
                        visibility = VISIBLE
                        when {
                            selectedItemId != R.id.nav_home -> selectedItemId = R.id.nav_home
                        }
                    }
                }

                setHasOptionsMenu(true)

                db = Firebase.database
                content = categoryView.findViewById(R.id.category_content)
                categoryItem = categoryView.findViewById(R.id.category_item)
                progressBar = categoryView.findViewById(R.id.category_progress_bar)

                val categoriesData = bookLiveData.categoriesData.value
                when {
                    categoriesData != null -> {
                        categoryList = categoriesData as ArrayList<Category>
                        setAdapter()
                    }
                    else -> {
                        db.reference.child("categories")
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    dataSnapshot.children.forEach {
                                        categoryList.add(castCategoryData(it.value as HashMap<*, *>))
                                    }
                                    setAdapter()

                                    bookLiveData.setCategories(categoryList)
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

                db.reference.child("favorites")
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            dataSnapshot.children.forEach {
                                if (auth.currentUser?.uid == it.key) {
                                    it.children.forEach { book ->
                                        favBookList.add(castBookData(book.value as HashMap<*, *>))
                                    }
                                }
                            }
                            bookLiveData.setFavBooks(favBookList)
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(
                                context, getString(R.string.something_wrong), Toast.LENGTH_LONG
                            ).show()
                        }
                    })
            }
        }
        return categoryView
    }

    override fun onSeeMoreClick(category: Category) {
        bookLiveData.setCategory(category)
        categoryView.findNavController().navigate(R.id.action_category_to_see_more)
    }

    override fun onBookClick(book: Books) {
        bookLiveData.setBook(book)
        categoryView.findNavController().navigate(R.id.action_category_to_book_details)
    }

    private fun setAdapter() {
        categoryItem.apply {
            layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.VERTICAL, false
            )
            adapter = CategoryAdapter(categoryList, this@CategoryFragment)
        }

        progressBar.visibility = GONE
        content.visibility = VISIBLE
    }

}