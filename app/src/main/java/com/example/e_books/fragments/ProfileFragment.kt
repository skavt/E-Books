package com.example.e_books.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.e_books.R
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment(R.layout.profile_fragment) {

    private lateinit var profileView: View
    private lateinit var auth: FirebaseAuth
    private lateinit var passField: TextView
    private lateinit var currentField: TextView
    private lateinit var emailDisplay: TextView
    private lateinit var cancelEditing: TextView
    private lateinit var changePassword: TextView
    private lateinit var settingsButton: TextView
    private lateinit var logOutButton: TextView
    private lateinit var mainContent: LinearLayout
    private lateinit var passRepeatField: TextView
    private lateinit var savePassword: AppCompatButton
    private lateinit var passwordChangeContent: CardView


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
        passField = profileView.findViewById(R.id.textPass)
        cancelEditing = profileView.findViewById(R.id.cancel)
        savePassword = profileView.findViewById(R.id.save_password)
        currentField = profileView.findViewById(R.id.textCurrentPass)
        passRepeatField = profileView.findViewById(R.id.textPassRepeat)
        changePassword = profileView.findViewById(R.id.change_password)
        emailDisplay = profileView.findViewById(R.id.email_display_text)
        mainContent = profileView.findViewById(R.id.profile_main_content)
        passwordChangeContent = profileView.findViewById(R.id.password_edit_content)
        settingsButton = profileView.findViewById(R.id.settings_button)
        logOutButton = profileView.findViewById(R.id.log_out)

        emailDisplay.text = auth.currentUser!!.email

        changePassword.setOnClickListener {
            updateUi(PASS_CHANGE_PRESSED)
        }

        cancelEditing.setOnClickListener {
            updateUi(PASS_CHANGE_FINISH)
        }

        settingsButton.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToSettingsFragment())
        }

        logOutButton.setOnClickListener {
            auth.signOut()
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToLoginFragment())
        }

        savePassword.setOnClickListener {
            val currentPassword = currentField.text.toString()
            when {
                validatePassword(currentField) -> {
                    auth.currentUser!!.email?.let { it1 ->
                        reAuth(it1, currentPassword, PASS_CHANGE_FINISH)
                    }
                }

            }
        }

        return profileView
    }

    private fun updateUi(action: String) {
        when (action) {
            PASS_CHANGE_PRESSED -> {
                mainContent.visibility = GONE
                passwordChangeContent.visibility = VISIBLE
            }
            PASS_CHANGE_FINISH -> {
                mainContent.visibility = VISIBLE
                passwordChangeContent.visibility = GONE

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

    private fun reAuth(email: String, pass: String, action: String) {
        val user = auth.currentUser
        val credential = EmailAuthProvider.getCredential(email, pass)

        user!!.reauthenticate(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
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
                                                getString(R.string.updated_password),
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
                Toast.makeText(context, getString(R.string.current_pass_error), Toast.LENGTH_LONG)
                    .show()
                currentField.error = getString(R.string.incorrect_password)
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