package com.example.e_books.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.e_books.R
import com.example.e_books.model.Books
import kotlinx.android.synthetic.main.category_book_item.view.*

class BookAdapter(
    private val bookList: ArrayList<Books>
) :
    RecyclerView.Adapter<BookAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.category_book_item, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setContent(bookList[position])
    }

    override fun getItemCount(): Int = bookList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun setContent(book: Books) {
            with(book) {
                itemView.test.text = book_id.toString()
            }
        }
    }
}