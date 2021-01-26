package com.example.e_books.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.e_books.R
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment(R.layout.profile_fragment) {
    private lateinit var profileView: View
    private lateinit var currentField: TextView
    private lateinit var passField: TextView
    private lateinit var passRepeatField: TextView
    private lateinit var logOut: Button
    private lateinit var emailDisplay: TextView
    private lateinit var passEdit: AppCompatButton
    private lateinit var auth: FirebaseAuth


    @SuppressLint("ShowToast")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        profileView = inflater.inflate(R.layout.profile_fragment, container, false)
        (activity as AppCompatActivity).apply {
            title = getString(R.string._profile)
            supportActionBar?.apply {
                show()
                setDisplayHomeAsUpEnabled(false)
            }
        }

        auth = Firebase.auth
        logOut = profileView.findViewById(R.id.log_out)
        passEdit = profileView.findViewById(R.id.changePassButton)
        currentField = profileView.findViewById(R.id.textCurrentPass)
        passField = profileView.findViewById(R.id.textPass)
        passRepeatField = profileView.findViewById(R.id.textPassRepeat)
        emailDisplay = profileView.findViewById(R.id.email_display_text)

        emailDisplay.text = auth.currentUser!!.email

        logOut.setOnClickListener {
            auth.signOut()
            profileView.findNavController().navigate(R.id.login_fragment)
        }


        passEdit.setOnClickListener {
            if (passField.isVisible) {
                val currentPassword = currentField.text.toString()
                when {
                    validatePassword(currentField) -> {
                        auth.currentUser!!.email?.let { it1 ->
                            reauth(
                                it1,
                                currentPassword,
                                PASS_CHANGE_FINISH
                            )
                        }
                    }

                }
            } else {
                updateUi(PASS_CHANGE_PRESSED)
            }
        }

        return profileView
    }

    private fun updateUi(action: String) {
        when (action) {
            PASS_CHANGE_PRESSED -> {
                currentField.visibility = VISIBLE
                passField.visibility = VISIBLE
                passRepeatField.visibility = VISIBLE
                passEdit.text = getString(R.string.save)
            }
            PASS_CHANGE_FINISH -> {
                currentField.visibility = GONE
                passField.visibility = GONE
                passRepeatField.visibility = GONE
                passEdit.text = getString(R.string.change_password)

                currentField.apply {
                    text = ""
                    error = null
                    clearFocus()
                }
                passField.apply {
                    text = ""
                    error = null
                    clearFocus()
                }
                passRepeatField.apply {
                    text = ""
                    error = null
                    clearFocus()
                }
            }
        }

    }

    private fun reauth(email: String, pass: String, action: String) {
        val user = auth.currentUser
        val credential = EmailAuthProvider
            .getCredential(email, pass)

        user!!.reauthenticate(credential)
            .addOnCompleteListener { task ->
                val ths = task
                if (task.isSuccessful) {
                    Log.d("RE-AUTH", "User re-authenticated.")

                    when (action) {
                        PASS_CHANGE_FINISH -> {
                            when {
                                validatePassword(passField) &&
                                        checkPasswords(passField, passRepeatField) -> {

                                    val password = passField.text.toString()
                                    auth.currentUser!!.updatePassword(password)
                                        .addOnCompleteListener { it ->
                                            if (it.isSuccessful) {
                                                Toast.makeText(
                                                    context,
                                                    "Password updated successfully",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                                updateUi(PASS_CHANGE_FINISH)
                                            }
                                        }
                                }

                            }
                        }
                    }
                } else {
                    Toast.makeText(context, "Current password is not correct", Toast.LENGTH_LONG)
                        .show()
                    currentField.error = "Incorrect password."
                }
            }
    }

    private fun checkPasswords(password: TextView, passwordRepeat: TextView): Boolean {
        val errorMessage = getString(R.string.not_match)

        return when {
            password.text.toString() == passwordRepeat.text.toString() -> true
            else -> {
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                passwordRepeat.apply {
                    error = errorMessage
                    requestFocus()
                }
                false
            }
        }
    }

    private fun validatePassword(password: TextView): Boolean {
        val lengthError = getString(R.string.invalid_password)
        val emptyError = getString(R.string.empty_password)

        return when {
            password.text.isEmpty() -> {
                Toast.makeText(context, emptyError, Toast.LENGTH_LONG).show()
                password.apply {
                    error = emptyError
                    requestFocusFromTouch()
                }
                false
            }
            password.text.toString().length < 6 -> {
                Toast.makeText(context, lengthError, Toast.LENGTH_LONG).show()
                password.apply {
                    error = lengthError
                    requestFocusFromTouch()
                }
                false
            }
            else -> true
        }
    }

    companion object Action {
        var PASS_CHANGE_PRESSED = "pass_change_pressed"
        var PASS_CHANGE_FINISH = "pass_change_finish"
    }

}