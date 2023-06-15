package com.travelsmartplus.travelsmartplus.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.travelsmartplus.travelsmartplus.R
import com.travelsmartplus.travelsmartplus.activities.MainActivity
import com.travelsmartplus.travelsmartplus.activities.SignUpActivity
import com.travelsmartplus.travelsmartplus.databinding.FragmentSetupFinishBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * SetupFinishFragment
 * Represents the last setup page with the confirmation.
 *
 * @author Gabriel Salas
 */

@AndroidEntryPoint
class SetupFinishFragment : Fragment() {

    private lateinit var binding: FragmentSetupFinishBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSetupFinishBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Go to the main activity
        binding.finishSetupBtn.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }
    }


}