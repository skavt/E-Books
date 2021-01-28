package com.example.e_books.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.bumptech.glide.Glide
import com.example.e_books.R
import com.example.e_books.model.Books
import com.example.e_books.services.BookLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlin.properties.Delegates

class BookDetailsFragment : Fragment(R.layout.book_details_fragment) {

    private lateinit var image: ImageView
    private lateinit var auth: FirebaseAuth
    private lateinit var bookTitle: TextView
    private lateinit var addToFav: ImageView
    private lateinit var db: FirebaseDatabase
    private lateinit var readerButton: Button
    private lateinit var bookAuthor: TextView
    private lateinit var bookDetailsView: View
    private lateinit var removeFromFav: ImageView
    private lateinit var bookPageNumbers: TextView
    private lateinit var bookDescription: TextView
    private var bookId by Delegates.notNull<Int>()

    private val bookLiveData: BookLiveData by navGraphViewModels(R.id.books_nav)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bookDetailsView = inflater.inflate(R.layout.book_details_fragment, container, false)
        setHasOptionsMenu(true)

        auth = Firebase.auth
        db = Firebase.database
        addToFav = bookDetailsView.findViewById(R.id.add_button)
        readerButton = bookDetailsView.findViewById(R.id.read_button)
        image = bookDetailsView.findViewById(R.id.book_details_image)
        removeFromFav = bookDetailsView.findViewById(R.id.remove_button)
        bookTitle = bookDetailsView.findViewById(R.id.book_details_title)
        bookAuthor = bookDetailsView.findViewById(R.id.book_details_author)
        bookDescription = bookDetailsView.findViewById(R.id.book_details_description)
        bookPageNumbers = bookDetailsView.findViewById(R.id.book_details_page_numbers)

        bookLiveData.bookData.observe(viewLifecycleOwner, { book ->
            with(book) {
                (activity as AppCompatActivity).title = name
                bookTitle.text = name
                bookDescription.text = description
                bookAuthor.text = author
                bookPageNumbers.text = pageNumbers
                bookId = book_id
                context?.let { Glide.with(it).load(imageUrl).into(image) }
                readerButton.setOnClickListener {
                    findNavController().navigate(R.id.action_book_details_to_pdf_view)
                }
            }
        })

        bookLiveData.favBooksLiveData.observe(viewLifecycleOwner, { books ->
            books.forEach {
                when (it.book_id) {
                    bookId -> {
                        addToFav.visibility = GONE
                        removeFromFav.visibility = VISIBLE
                    }
                }
            }
        })

        addToFav.setOnClickListener {
            val data = bookLiveData.bookData.value as Books

            val childUpdates = hashMapOf<String, Any>(
                "/favorites/${auth.currentUser?.uid}/$bookId" to data,
            )

            db.reference.updateChildren(childUpdates)

            addToFav.visibility = GONE
            removeFromFav.visibility = VISIBLE
        }

        removeFromFav.setOnClickListener {
            db.reference.child("/favorites/${auth.currentUser?.uid}/$bookId").removeValue()

            addToFav.visibility = VISIBLE
            removeFromFav.visibility = GONE
        }

        return bookDetailsView
    }
}