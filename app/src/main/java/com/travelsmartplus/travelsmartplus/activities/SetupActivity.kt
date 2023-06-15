package com.travelsmartplus.travelsmartplus.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.travelsmartplus.travelsmartplus.R
import com.travelsmartplus.travelsmartplus.databinding.ActivitySetupBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * SetupActivity
 * Represents the setup activity. It host the Setup fragments and nav.
 *
 * @author Gabriel Salas
 */

@AndroidEntryPoint
class SetupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySetupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the NavController with the NavHostFragment
        supportFragmentManager.findFragmentById(R.id.setupNavHost) as NavHostFragment


    }
}