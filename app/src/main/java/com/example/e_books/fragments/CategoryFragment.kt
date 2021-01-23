package com.example.e_books.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_books.R
import com.example.e_books.adapters.CategoryAdapter
import com.example.e_books.extentions.castCategoryData
import com.example.e_books.extentions.castFavBookData
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


class CategoryFragment : Fragment(R.layout.category_fragment), CategoryAdapter.OnItemClickListener {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var categoryView: View
    private lateinit var categoryItem: RecyclerView
    private val categoryList = ArrayList<Category>()
    private val bookList = ArrayList<Books>()
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

        when (auth.currentUser) {
            null -> findNavController().navigate(R.id.action_category_to_login)
            else -> {
                db = Firebase.database
                categoryItem = categoryView.findViewById(R.id.category_item)

                db.reference.child("categories")
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            dataSnapshot.children.forEach {
                                categoryList.add(castCategoryData(it.value as HashMap<*, *>))
                            }
                            categoryItem.layoutManager = LinearLayoutManager(
                                context,
                                LinearLayoutManager.VERTICAL,
                                false
                            )
                            categoryItem.adapter = CategoryAdapter(
                                categoryList,
                                this@CategoryFragment
                            )
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })

                db.reference.child("favorites")
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            dataSnapshot.children.forEach {
                                if (auth.currentUser?.uid == it.key) {
                                    it.children.forEach { book ->
                                        bookList.add(castFavBookData(book.value as HashMap<*, *>))
                                    }
                                }
                            }
                            bookLiveData.setFavBooks(bookList)
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

}