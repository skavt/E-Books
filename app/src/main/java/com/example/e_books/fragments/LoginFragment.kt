package com.example.e_books.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.e_books.R
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginFragment : Fragment(R.layout.login_fragment) {

    private var auth = Firebase.auth
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button
    private lateinit var textEmail: TextView
    private lateinit var textPass: TextView
    private lateinit var textPassRepeat: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textEmail = view.findViewById(R.id.textEmail)
        textPass = view.findViewById(R.id.textPass)
        textPassRepeat = view.findViewById(R.id.textPassRepeat)
        textPassRepeat.visibility = View.GONE
        loginButton = view.findViewById(R.id.loginButton)
        registerButton = view.findViewById(R.id.registerButton)

        auth = Firebase.auth

        updateUiState(auth.currentUser)

        auth.signOut()
        registerButton.setOnClickListener {

            if (textPassRepeat.visibility == View.VISIBLE) {
                val email = textEmail.text.toString()
                val password = textPass.text.toString()
                if (validateEmail(textEmail) && validatePassword(textPass) && checkPasswords(
                        textPass,
                        textPassRepeat
                    )
                ) {
                    register(email, password)
                }
            } else {
                textPassRepeat.visibility = View.VISIBLE
                loginButton.visibility = View.GONE
            }

        }

        loginButton.setOnClickListener {
            if (auth.currentUser != null) {
                signOut()
                updateUiState(null)
            } else {
                val email = textEmail.text.toString()
                val pass = textPass.text.toString()
                if (validateEmail(textEmail) && validatePassword(textPass)) {
                    signIn(email, pass)
                }
            }
        }
    }

    private fun checkPasswords(password: TextView, passwordRepeat: TextView): Boolean {
        val error = "Passwords does not match!"

        return if (password.text.toString() == passwordRepeat.text.toString()) true
        else {
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            passwordRepeat.error = error

            false
        }
    }

    private fun validatePassword(password: TextView): Boolean {
        val error = "Password must be minimum 6 symbols!"

        return if (password.text.toString().length < 6) {
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            password.error = error
            false
        } else true
    }

    private fun validateEmail(email: TextView): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        var error = ""

        return if (email.text.toString().isEmpty()) {
            error = "Invalid email address"
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            email.error = error
            false
        } else {
            if (email.text.toString().trim { it <= ' ' }.matches(emailPattern.toRegex())) {
                true
            } else {
                error = "Invalid email address"
                Toast.makeText(context, error, Toast.LENGTH_SHORT)
                    .show()
                email.error = error
                false
            }
        }
    }

    private fun signOut() {
        auth.signOut()
    }

    private fun updateUiState(user: FirebaseUser?) {
        user?.let {
            loginButton.text = getString(R.string.log_out)
            textEmail.visibility = View.GONE
            textPass.visibility = View.GONE
            registerButton.visibility = View.GONE

        } ?: run {
            loginButton.text = getString(R.string.log_in)
            textEmail.visibility = View.VISIBLE
            textPass.visibility = View.VISIBLE
        }
    }

    private fun register(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    Toast.makeText(context, "Hello, ${user?.email}", Toast.LENGTH_LONG).show()
                    findNavController().popBackStack()
                } else {
                    // If registration fails, display a message to the user.
                    Log.w("registration", "createUserWithEmail:failure", task.exception)

                    Toast.makeText(
                        context, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener() {task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(context, "Hello, ${user?.email}", Toast.LENGTH_LONG).show()
                    findNavController().popBackStack()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("singIn", "signInWithEmail:failure", task.exception)
                    Toast.makeText(context, task.exception.toString(),
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
}