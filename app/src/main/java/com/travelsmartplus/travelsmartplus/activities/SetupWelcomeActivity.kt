package com.travelsmartplus.travelsmartplus.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.travelsmartplus.travelsmartplus.databinding.ActivityWelcomeSetupBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * SetupWelcomeActivity
 * Represents the welcome setup activity.
 *
 * @author Gabriel Salas
 */

@AndroidEntryPoint
class SetupWelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeSetupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeSetupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.setupStartBtn.setOnClickListener {
            val intent = Intent(this, SetupPasswordActivity::class.java)
            startActivity(intent)
        }
    }
}