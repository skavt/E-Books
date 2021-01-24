package com.example.e_books.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_books.R
import com.example.e_books.adapters.CategoryAdapter
import com.example.e_books.adapters.FavoritesAdapter
import com.example.e_books.extentions.castFavBookData
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

class FavoritesFragment : Fragment(R.layout.favorites_fragment),
    CategoryAdapter.OnItemClickListener {

    private lateinit var auth: FirebaseAuth
    private lateinit var favoritesView: View
    private lateinit var db: FirebaseDatabase
    private lateinit var favoriteItem: RecyclerView
    private lateinit var noData: LinearLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var content: LinearLayout
    private var bookList = ArrayList<Books>()
    private val bookLiveData: BookLiveData by navGraphViewModels(R.id.books_nav)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        favoritesView = inflater.inflate(R.layout.favorites_fragment, container, false)
        (activity as AppCompatActivity).apply {
            supportActionBar?.show()
            title = getString(R.string.favorites)
        }
        setHasOptionsMenu(true)

        noData = favoritesView.findViewById(R.id.no_fav_data)
        progressBar = favoritesView.findViewById(R.id.favorites_progress_bar)
        content = favoritesView.findViewById(R.id.favorites_content)

        auth = Firebase.auth

        when (auth.currentUser) {
            null -> findNavController().navigate(R.id.login_fragment)
            else -> {
                db = Firebase.database
                favoriteItem = favoritesView.findViewById(R.id.favorites_item)

                db.reference.child("favorites")
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            dataSnapshot.children.forEach {
                                when (auth.currentUser?.uid) {
                                    it.key -> {
                                        it.children.forEach { book ->
                                            when {
                                                book.exists() -> noData.visibility = GONE
                                                else -> noData.visibility = VISIBLE
                                            }
                                            bookList.add(castFavBookData(book.value as HashMap<*, *>))
                                        }
                                    }
                                }
                            }
                            favoriteItem.layoutManager = LinearLayoutManager(
                                context,
                                LinearLayoutManager.VERTICAL,
                                false
                            )
                            favoriteItem.adapter = FavoritesAdapter(
                                bookList,
                                this@FavoritesFragment
                            )
                            bookLiveData.setFavBooks(bookList)
                            progressBar.visibility = GONE
                            content.visibility = VISIBLE
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })
            }
        }

        return favoritesView
    }

    override fun onSeeMoreClick(category: Category) {
        TODO("Not yet implemented")
    }

    override fun onBookClick(book: Books) {
        bookLiveData.setBook(book)
        favoritesView.findNavController().navigate(R.id.book_details_fragment)
    }
}