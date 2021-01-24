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

class FavoritesFragment : Fragment(R.layout.favorites_fragment),
    CategoryAdapter.OnItemClickListener {

    private var bookList = ArrayList<Books>()

    private lateinit var auth: FirebaseAuth
    private lateinit var favoritesView: View
    private lateinit var noData: LinearLayout
    private lateinit var db: FirebaseDatabase
    private lateinit var content: LinearLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var favoriteItem: RecyclerView

    private val bookLiveData: BookLiveData by navGraphViewModels(R.id.books_nav)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        favoritesView = inflater.inflate(R.layout.favorites_fragment, container, false)
        (activity as AppCompatActivity).apply {
            title = getString(R.string._favorites)
            supportActionBar?.apply {
                show()
                setDisplayHomeAsUpEnabled(false)
            }
        }

        auth = Firebase.auth
        db = Firebase.database
        noData = favoritesView.findViewById(R.id.no_fav_data)
        content = favoritesView.findViewById(R.id.favorites_content)
        favoriteItem = favoritesView.findViewById(R.id.favorites_item)
        progressBar = favoritesView.findViewById(R.id.favorites_progress_bar)


        when (auth.currentUser) {
            null -> findNavController().navigate(R.id.login_fragment)
            else -> {
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
                                            bookList.add(castBookData(book.value as HashMap<*, *>))
                                        }
                                    }
                                }
                            }
                            favoriteItem.apply {
                                layoutManager = LinearLayoutManager(
                                    context, LinearLayoutManager.VERTICAL, false
                                )
                                adapter = FavoritesAdapter(
                                    bookList, this@FavoritesFragment
                                )
                            }
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