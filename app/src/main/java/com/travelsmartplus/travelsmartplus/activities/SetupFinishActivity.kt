package com.travelsmartplus.travelsmartplus.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.travelsmartplus.travelsmartplus.databinding.ActivitySetupFinishBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * SetupFinishActivity
 * Represents the last setup page.
 *
 * @author Gabriel Salas
 */

@AndroidEntryPoint
class SetupFinishActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySetupFinishBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetupFinishBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.finishSetupBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()  // Close the setup activities to prevent going back
        }
    }
}