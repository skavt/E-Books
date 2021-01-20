package com.example.e_books.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_books.R
import com.example.e_books.adapters.CategoryDetailsAdapter
import com.example.e_books.model.Books
import com.example.e_books.services.BookLiveData
import kotlinx.android.synthetic.main.category_details_fragment.*

class CategoryDetailsFragment : Fragment(R.layout.category_details_fragment) {

    private lateinit var categoryDetailsView: View
    private val bookLiveData: BookLiveData by navGraphViewModels(R.id.books_nav)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        categoryDetailsView = inflater.inflate(R.layout.category_details_fragment, container, false)
        setHasOptionsMenu(true)

        bookLiveData.categoryData.observe(viewLifecycleOwner, { category ->
            with(category) {
                (activity as AppCompatActivity).title = category_name
                category_details_item.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                category_details_item.adapter = CategoryDetailsAdapter(books as ArrayList<Books>)
            }
        })

        return categoryDetailsView
    }

}