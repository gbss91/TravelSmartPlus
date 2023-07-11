package com.travelsmartplus.travelsmartplus.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.travelsmartplus.travelsmartplus.databinding.ActivityLandingPageBinding

/**
 * LandingPageActivity
 * Represents the app landing page with the button to sign in and sign up.
 *
 * @author Gabriel Salas
 */

class LandingPageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLandingPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLandingPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.landingSignInBtn.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        binding.landingSignUpBtn.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

    }

}