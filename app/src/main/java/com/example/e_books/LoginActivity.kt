package com.example.e_books

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var registrationButton: Button
    private lateinit var registrationEmail: TextView
    private lateinit var registrationPass: TextView

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        registrationEmail = findViewById(R.id.registerEmail)
        registrationPass = findViewById(R.id.registerPass)
        registrationButton = findViewById(R.id.registerButton)

        auth = Firebase.auth

        updateUiState(auth.currentUser)


        registrationButton.setOnClickListener {

            if (auth.currentUser != null) {
                signOut()
                updateUiState(null)
            } else {
                val email = registrationEmail.text.toString()
                val password = registrationPass.text.toString()
                register(email, password)
            }
        }




    }

    private fun signOut() {
        auth.signOut()
    }

    fun updateUiState(user: FirebaseUser?) {
        user?.let {
            registrationButton.text = "Log Out"
            registrationEmail.visibility = View.GONE
            registrationPass.visibility = View.GONE
            Toast.makeText(baseContext, "Hello, ${it.uid}", Toast.LENGTH_LONG).show()

        } ?: run {
            registrationButton.text = "Register"
            registrationEmail.visibility = View.VISIBLE
            registrationPass.visibility = View.VISIBLE
        }
    }

    private fun register(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    Toast.makeText(baseContext, "Hello, ${user?.uid}", Toast.LENGTH_LONG).show()
                    updateUiState(auth.currentUser)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.d("login", "signInWithCustomToken:failure", task.exception)

                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }


    }
}