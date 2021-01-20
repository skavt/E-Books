package com.example.e_books.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_books.R
import com.example.e_books.model.Books
import com.example.e_books.model.Category
import kotlinx.android.synthetic.main.category_item.view.*

class CategoryAdapter(
    private val categoryList: ArrayList<Category>
) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.category_item, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setContent(categoryList[position])
    }

    override fun getItemCount(): Int = categoryList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun setContent(category: Category) {
            with(category) {
                itemView.category_name.text = category_name

                itemView.category_book_item.layoutManager =
                    LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
                itemView.category_book_item.adapter = BookAdapter(books as ArrayList<Books>)
            }
        }
    }
}