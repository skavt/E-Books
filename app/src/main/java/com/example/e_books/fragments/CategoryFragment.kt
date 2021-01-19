package com.example.e_books.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_books.R
import com.example.e_books.adapters.CategoryAdapter
import com.example.e_books.model.Books
import com.example.e_books.model.Category
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.category_fragment.*


class CategoryFragment : Fragment(R.layout.category_fragment) {

    private lateinit var db: FirebaseDatabase
    private lateinit var model: Category

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = Firebase.database

        db.reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.forEach {
                    val memberList = it.value as ArrayList<*>
                    category_item.layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    category_item.adapter = CategoryAdapter(castData(memberList))
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun castData(categoryData: ArrayList<*>): ArrayList<Category> {
        val size = categoryData.size
        var index = 0
        val categoryList = ArrayList<Category>()

        while (index < size) {
            val mapCategoryData: HashMap<*, *> = categoryData[index] as HashMap<*, *>
            val id = mapCategoryData["id"].toString().toInt()
            val categoryName = mapCategoryData["category_name"].toString()
            val books = mapCategoryData["books"] as ArrayList<*>

            categoryList.add(Category(id, categoryName, castBookData(books)))
            index++
        }
        return categoryList
    }

    fun castBookData(bookData: ArrayList<*>): ArrayList<Books> {
        val size = bookData.size
        var index = 0
        val bookList = ArrayList<Books>()

        while (index < size) {
            val mapBookData: HashMap<*, *> = bookData[index] as HashMap<*, *>
            val bookId = mapBookData["book_id"].toString().toInt()
//            val categoryName = mapBookData["category_name"].toString()
//            val books = mapBookData["books"] as ArrayList<*>

            bookList.add(Books(bookId))
            index++
        }
        return bookList
    }
}