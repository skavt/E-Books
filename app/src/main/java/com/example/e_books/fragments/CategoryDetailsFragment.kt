package com.example.e_books.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_books.R
import com.example.e_books.adapters.CategoryAdapter
import com.example.e_books.adapters.CategoryDetailsAdapter
import com.example.e_books.model.Books
import com.example.e_books.model.Category
import com.example.e_books.services.BookLiveData

class CategoryDetailsFragment : Fragment(R.layout.category_details_fragment),
    CategoryAdapter.OnItemClickListener {

    private lateinit var categoryDetailsView: View
    private lateinit var categoryDetailsItem: RecyclerView
    private val bookLiveData: BookLiveData by navGraphViewModels(R.id.books_nav)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        categoryDetailsView = inflater.inflate(R.layout.category_details_fragment, container, false)
        setHasOptionsMenu(true)

        categoryDetailsItem = categoryDetailsView.findViewById(R.id.category_details_item)

        bookLiveData.categoryData.observe(viewLifecycleOwner, { category ->
            with(category) {
                (activity as AppCompatActivity).title = category_name
                categoryDetailsItem.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                categoryDetailsItem.adapter =
                    CategoryDetailsAdapter(books as ArrayList<Books>, this@CategoryDetailsFragment)
            }
        })

        return categoryDetailsView
    }

    override fun onSeeMoreClick(category: Category) {
        TODO("Not yet implemented")
    }

    override fun onBookClick(book: Books) {
        bookLiveData.setBook(book)
        categoryDetailsView.findNavController().navigate(R.id.book_details_fragment)
    }

}