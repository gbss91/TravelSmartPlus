package com.travelsmartplus.travelsmartplus.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.travelsmartplus.travelsmartplus.databinding.ActivityMainBinding
import com.travelsmartplus.travelsmartplus.viewModels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Observers - observes responses and errors
        mainViewModel.isSignedIn.observe(this) { signedIn ->
            if (!signedIn) {
                val intent = Intent(this, LandingPageActivity::class.java)
                startActivity(intent)
            }
        }
    }
}