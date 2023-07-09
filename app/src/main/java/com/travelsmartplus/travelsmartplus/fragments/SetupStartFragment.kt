package com.travelsmartplus.travelsmartplus.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.travelsmartplus.travelsmartplus.R
import com.travelsmartplus.travelsmartplus.databinding.FragmentSetupStartBinding

/**
 * SetupStartFragment
 * Represents start view for the account setup.
 *
 * @author Gabriel Salas
 */

class SetupStartFragment : Fragment() {

    private lateinit var binding: FragmentSetupStartBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSetupStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.setupStartBtn.setOnClickListener {
            findNavController().navigate(R.id.action_setupStartFragment_to_setupPasswordFragment)
        }

        // Set GIF image
        Glide.with(this)
            .asGif()
            .load(R.drawable.settings)
            .into(binding.setupStartIcon)

    }
}