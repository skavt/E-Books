package com.example.e_books.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_books.R
import com.example.e_books.adapters.CategoryAdapter
import com.example.e_books.model.Category
import com.example.e_books.services.BookModel
import kotlinx.android.synthetic.main.category_fragment.*

class CategoryFragment : Fragment(R.layout.category_fragment) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = BookModel()
        viewModel.bookLiveData.observe(
            viewLifecycleOwner,
            {
                category_item.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                category_item.adapter = CategoryAdapter(it.categories as ArrayList<Category>)
            }
        )
        viewModel.getBooks()
    }
}