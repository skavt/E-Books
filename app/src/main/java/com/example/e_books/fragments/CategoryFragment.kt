package com.example.e_books.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.e_books.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class CategoryFragment : Fragment(R.layout.category_fragment) {
    private val auth = Firebase.auth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()

        auth.addAuthStateListener { auth ->
            Log.d("Auth", "onViewCreated: AuthListener entered")

            if (auth.currentUser == null) {
                navController.navigate(R.id.login_fragment)
            }
        }
    }

}