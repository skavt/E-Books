package com.example.e_books.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.e_books.R
import com.example.e_books.model.Books
import com.example.e_books.model.Category
import com.example.e_books.model.FavoriteBooks
import kotlinx.android.synthetic.main.favorites_item.view.*

class FavoritesAdapter(
    private val bookList: ArrayList<FavoriteBooks>,
    private val listener: OnFavItemClickListener
) :
    RecyclerView.Adapter<FavoritesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.favorites_item, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setContent(bookList[position])
    }

    override fun getItemCount(): Int = bookList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun setContent(book: FavoriteBooks) {
            with(book) {
                itemView.favorites_name.text = name
                itemView.favorites_author.text = author
                itemView.favorites_description.text = description
                Glide.with(itemView.context).load(imageUrl).into(itemView.favorites_book_image)

                itemView.favorites_book_image.setOnClickListener {
                    listener.onBookClick(book)
                }

            }
        }
    }

    interface OnFavItemClickListener {
        fun onBookClick(book: FavoriteBooks)
    }
}