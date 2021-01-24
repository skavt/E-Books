package com.example.e_books.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.e_books.R
import com.example.e_books.model.Books
import kotlinx.android.synthetic.main.search_item.view.*

class SearchAdapter(
    private val bookList: ArrayList<Books>,
    private val listener: CategoryAdapter.OnItemClickListener
) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.search_item, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setContent(bookList[position])
    }

    override fun getItemCount(): Int = bookList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun setContent(book: Books) {
            with(book) {
                itemView.search_name.text = name
                itemView.search_author.text = author
                itemView.search_description.text = description
                Glide.with(itemView.context).load(imageUrl).into(itemView.search_book_image)

                itemView.setOnClickListener {
                    listener.onBookClick(book)
                }

            }
        }
    }
}