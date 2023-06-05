package com.travelsmartplus.travelsmartplus.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.travelsmartplus.travelsmartplus.R
import com.travelsmartplus.travelsmartplus.databinding.ActivityMainBinding
import com.travelsmartplus.travelsmartplus.fragments.AllBookingsFragment
import com.travelsmartplus.travelsmartplus.fragments.BookingSearchFragment
import com.travelsmartplus.travelsmartplus.fragments.MyBookingsFragment
import com.travelsmartplus.travelsmartplus.fragments.ProfileFragment
import com.travelsmartplus.travelsmartplus.fragments.UsersFragment
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

        // Load the BookingSearchFragment at the start of the activity
        replaceFragment(BookingSearchFragment())

        // Clear the selected item using hidden placeholder
        binding.bottomNavigationView.selectedItemId = R.id.bnbPlaceholder

        binding.mainBtn.setOnClickListener {
            binding.bottomNavigationView.menu.setGroupCheckable(0, false, true)
            replaceFragment(BookingSearchFragment())
        }

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            binding.bottomNavigationView.menu.setGroupCheckable(0, true, true)
            when (item.itemId) {
                R.id.btnMenuBookings -> { replaceFragment(MyBookingsFragment())
                    true
                }
                R.id.btnMenuAllBookings -> {
                    replaceFragment(AllBookingsFragment())
                    true
                }
                R.id.btnMenuUsers -> {
                    replaceFragment(UsersFragment())
                    true
                }
                R.id.btnMenuProfile -> {
                    replaceFragment(ProfileFragment())
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

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.setReorderingAllowed(true)
        transaction.replace(R.id.mainFragmentContainer, fragment)
        transaction.commit()
    }
}