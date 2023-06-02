package com.travelsmartplus.travelsmartplus.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.travelsmartplus.travelsmartplus.R

/**
 * SetupPreferencesActivity
 * Represents the preferences setup activity. Lets a new user to add preferences to their account.
 *
 * @author Gabriel Salas
 */

class SetupPreferencesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_preferences)
    }
}