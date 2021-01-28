package com.example.e_books.fragments

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.example.e_books.R
import com.firebase.ui.auth.AuthUI.getApplicationContext

class SettingsFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    @SuppressLint("RestrictedApi")
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        (activity as AppCompatActivity).apply {
            title = getString(R.string._settings)
            supportActionBar?.apply {
                show()
            }
        }

        PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
            .registerOnSharedPreferenceChangeListener(this)
    }

    @SuppressLint("RestrictedApi")
    override fun onDestroy() {
        super.onDestroy()
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
            .unregisterOnSharedPreferenceChangeListener(this)
    }

    @SuppressLint("RestrictedApi")
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {

        val darkModeString = getString(R.string.dark_mode)
        key?.let {
            if (it == darkModeString) sharedPreferences?.let { pref ->
                val darkModeValues = resources.getStringArray(R.array.dark_mode_values)
                when (pref.getString(darkModeString, darkModeValues[0])) {
                    darkModeValues[0] -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    darkModeValues[1] -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
            }
        }

    }
}