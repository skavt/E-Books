package com.example.e_books.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.e_books.R
import com.example.e_books.model.Books
import kotlinx.android.synthetic.main.favorites_item.view.*

class FavoritesAdapter(
    private val bookList: ArrayList<Books>,
    private val listener: CategoryAdapter.OnItemClickListener
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

        fun setContent(book: Books) {
            with(book) {
                itemView.favorites_name.text = name
//                Glide.with(itemView.context).load(imageUrl).into(itemView.book_image)

//                itemView.book_image.setOnClickListener {
//                    listener.onBookClick(book)
//                }

            }
        }
    }
}