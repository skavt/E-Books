package com.example.e_books.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
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

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var categoryView: View
    private lateinit var categoryItem: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var content: NestedScrollView
    private lateinit var logOut: Button
    private var categoryList = ArrayList<Category>()
    private val favBookList = ArrayList<Books>()
    private val bookLiveData: BookLiveData by navGraphViewModels(R.id.books_nav)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        categoryView = inflater.inflate(R.layout.category_fragment, container, false)
        (activity as AppCompatActivity).apply {
            supportActionBar?.show()
            title = getString(R.string.app_name)
        }
        val navBar: BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        navBar.visibility = VISIBLE

        setHasOptionsMenu(true)

        auth = Firebase.auth
        db = Firebase.database
        logOut = categoryView.findViewById(R.id.log_out)
        content = categoryView.findViewById(R.id.category_content)
        progressBar = categoryView.findViewById(R.id.category_progress_bar)
        categoryItem = categoryView.findViewById(R.id.category_item)

        // TODO Delete this code after add user page
        logOut.setOnClickListener {
            auth.signOut()
            categoryView.findNavController().navigate(R.id.login_fragment)
        }

        when (auth.currentUser) {
            null -> findNavController().navigate(R.id.action_category_to_login)
            else -> {
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
                                    TODO("Not yet implemented")
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
                            TODO("Not yet implemented")
                        }
                    })
            }
        }

        return categoryView
    }

    override fun onSeeMoreClick(category: Category) {
        bookLiveData.setCategory(category)
        categoryView.findNavController().navigate(R.id.category_details_fragment)
    }

    override fun onBookClick(book: Books) {
        bookLiveData.setBook(book)
        categoryView.findNavController().navigate(R.id.book_details_fragment)
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