package com.example.e_books.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.e_books.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginFragment : Fragment(R.layout.login_fragment) {

    private lateinit var auth: FirebaseAuth
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button
    private lateinit var textEmail: TextView
    private lateinit var textPass: TextView
    private lateinit var textPassRepeat: TextView
    private lateinit var loginView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        loginView = inflater.inflate(R.layout.login_fragment, container, false)
        (activity as AppCompatActivity).apply {
            title = getString(R.string.sign_in)
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }
        setHasOptionsMenu(true)

        textEmail = loginView.findViewById(R.id.textEmail)
        textPass = loginView.findViewById(R.id.textPass)
        textPassRepeat = loginView.findViewById(R.id.textPassRepeat)
        loginButton = loginView.findViewById(R.id.loginButton)
        registerButton = loginView.findViewById(R.id.registerButton)

        auth = Firebase.auth

        updateUiState(auth.currentUser)

        auth.signOut()
        registerButton.setOnClickListener {
            (activity as AppCompatActivity).apply {
                title = getString(R.string.register)
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }

            when (textPassRepeat.visibility) {
                VISIBLE -> {
                    val email = textEmail.text.toString()
                    val password = textPass.text.toString()
                    when {
                        validateEmail(textEmail) && validatePassword(textPass) &&
                                checkPasswords(textPass, textPassRepeat) -> {
                            register(email, password)
                        }
                    }
                }
                else -> {
                    textPassRepeat.visibility = VISIBLE
                    loginButton.visibility = GONE
                    textEmail.apply {
                        text = ""
                        error = null
                    }
                    textPass.apply {
                        text = ""
                        error = null
                    }
                }
            }
        }

        loginButton.setOnClickListener {
            when {
                auth.currentUser != null -> {
                    signOut()
                    updateUiState(null)
                }
                else -> {
                    val email = textEmail.text.toString()
                    val pass = textPass.text.toString()
                    when {
                        validateEmail(textEmail) && validatePassword(textPass) -> {
                            signIn(email, pass)
                        }
                    }
                }
            }
        }
        return loginView
    }

    private fun checkPasswords(password: TextView, passwordRepeat: TextView): Boolean {
        val error = getString(R.string.not_match)

        return when {
            password.text.toString() == passwordRepeat.text.toString() -> true
            else -> {
                Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                passwordRepeat.error = error
                false
            }
        }
    }

    private fun validatePassword(password: TextView): Boolean {
        val error = getString(R.string.invalid_password)

        return when {
            password.text.toString().length < 6 && loginButton.visibility == GONE -> {
                Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                password.error = error
                false
            }
            else -> true
        }
    }

    private fun validateEmail(email: TextView): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        val error = getString(R.string.invalid_email)

        return when {
            email.text.toString().isEmpty() -> {
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                email.error = error
                false
            }
            else -> {
                when {
                    email.text.toString().trim { it <= ' ' }.matches(emailPattern.toRegex()) -> true
                    else -> {
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                        email.error = error
                        false
                    }
                }
            }
        }
    }

    private fun signOut() {
        auth.signOut()
    }

    private fun updateUiState(user: FirebaseUser?) {
        user?.let {
            loginButton.text = getString(R.string.log_out)
            textEmail.visibility = GONE
            textPass.visibility = GONE
            registerButton.visibility = GONE

        } ?: run {
            loginButton.text = getString(R.string.sign_in)
            textEmail.visibility = VISIBLE
            textPass.visibility = VISIBLE
        }
    }

    private fun register(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            when {
                it.isSuccessful -> {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    Toast.makeText(context, "Hello, ${user?.email}", Toast.LENGTH_LONG).show()
                    findNavController().popBackStack()
                }
                else -> {
                    // If registration fails, display a message to the user.
                    Toast.makeText(context, getString(R.string.failed_register), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun signIn(email: String, password: String) {
        val error = getString(R.string.failed_login)
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            when {
                it.isSuccessful -> {
                    val user = auth.currentUser
                    Toast.makeText(context, "Hello, ${user?.email}", Toast.LENGTH_LONG).show()
                    findNavController().popBackStack()
                }
                else -> {
                    // If sign in fails, display a message to the user.
                    textEmail.error = error
                    textPass.error = error
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}