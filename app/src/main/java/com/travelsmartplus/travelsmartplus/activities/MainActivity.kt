package com.travelsmartplus.travelsmartplus.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
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

        // Back to booking search
        val backToBookingSearch = NavOptions.Builder().setPopUpTo(R.id.bookingSearchFragment, false).build()

        binding.mainBtn.setOnClickListener {
            binding.bottomNavigationView.menu.setGroupCheckable(0, false, true)
            navController.navigate(R.id.action_global_bookingSearchFragment)
        }

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.btnMenuBookings -> {
                    navController.navigate(R.id.myBookingsFragment, null, backToBookingSearch)
                    true
                }
                R.id.btnMenuAllBookings -> {
                    navController.navigate(R.id.allBookingsFragment, null, backToBookingSearch)
                    true
                }
                R.id.btnMenuUsers -> {
                    navController.navigate(R.id.usersFragment, null, backToBookingSearch)
                    true
                }
                R.id.btnMenuProfile -> {
                    navController.navigate(R.id.profileFragment, null, backToBookingSearch)
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
                        val intent = Intent(this, SetupActivity::class.java)
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