package com.travelsmartplus.travelsmartplus.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.travelsmartplus.travelsmartplus.R
import com.travelsmartplus.travelsmartplus.databinding.ActivityMainBinding
import com.travelsmartplus.travelsmartplus.viewModels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * MainActivity
 * Represents the main activity of the Android app.
 *
 * @author Gabriel Salas
 */

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the NavController with the NavHostFragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.mainNavHost) as NavHostFragment
        val navController = navHostFragment.navController

        // Clear the selected item using hidden placeholder
        binding.bottomNavigationView.selectedItemId = R.id.bnbPlaceholder

        binding.mainBtn.setOnClickListener {
            navController.navigate(R.id.bookingSearchFragment)
        }

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.btnMenuBookings -> {
                    navController.navigate(R.id.myBookingsFragment)
                    true
                }
                R.id.btnMenuAllBookings -> {
                    navController.navigate(R.id.allBookingsFragment)
                    true
                }
                R.id.btnMenuUsers -> {
                    navController.navigate(R.id.usersFragment)
                    true
                }
                R.id.btnMenuProfile -> {
                    navController.navigate(R.id.profileFragment)
                    true
                }
                else -> false
            }
        }

        // Observers - observes responses and errors
        mainViewModel.isSignedIn.observe(this) { signedIn ->
            if (signedIn) {
                mainViewModel.isSetup.observe(this) { isSetup ->
                    if (!isSetup) {
                        val intent = Intent(this, SetupWelcomeActivity::class.java)
                        startActivity(intent)
                        finish() // Avoids returning when pressing back button
                    }
                }
            } else {
                val intent = Intent(this, LandingPageActivity::class.java)
                startActivity(intent)
                finish() // Avoids returning when pressing back button
            }
        }
    }

}