package com.example.e_books.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
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
import com.example.e_books.adapters.FavoritesAdapter
import com.example.e_books.extentions.castFavoritesData
import com.example.e_books.model.Books
import com.example.e_books.model.Category
import com.example.e_books.model.FavoriteBooks
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

class FavoritesFragment : Fragment(R.layout.favorites_fragment),
    CategoryAdapter.OnItemClickListener {

    private lateinit var auth: FirebaseAuth
    private lateinit var favoritesView: View
    private lateinit var db: FirebaseDatabase
    private lateinit var favoriteItem: RecyclerView
    lateinit var data: HashMap<*, *>
    val favoriteBookList = ArrayList<FavoriteBooks>()
    private val bookLiveData: BookLiveData by navGraphViewModels(R.id.books_nav)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        favoritesView = inflater.inflate(R.layout.favorites_fragment, container, false)
        (activity as AppCompatActivity).apply {
            supportActionBar?.show()
            title = getString(R.string.favorite)
        }
        setHasOptionsMenu(true)

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
                                auth.currentUser?.uid?.let { it1 ->
                                    val userUid = it.child(it1)
                                    when {
                                        userUid.exists() -> {
                                            favoriteItem.layoutManager = LinearLayoutManager(
                                                context,
                                                LinearLayoutManager.VERTICAL,
                                                false
                                            )
                                            favoriteItem.adapter = FavoritesAdapter(
                                                castFavoritesData(userUid.value as ArrayList<*>),
                                                this@FavoritesFragment
                                            )
                                        }
                                        // TODO add no data fragment
                                    }
                                }
                            }
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
        bookLiveData.setFavoriteBook(book)
        favoritesView.findNavController().navigate(R.id.favorites_fragment)
    }
}