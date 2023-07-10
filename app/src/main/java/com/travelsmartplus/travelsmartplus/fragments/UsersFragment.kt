package com.travelsmartplus.travelsmartplus.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.travelsmartplus.travelsmartplus.R
import com.travelsmartplus.travelsmartplus.adapters.OnItemClickListener
import com.travelsmartplus.travelsmartplus.adapters.UsersAdapter
import com.travelsmartplus.travelsmartplus.databinding.FragmentUsersBinding
import com.travelsmartplus.travelsmartplus.viewModels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * UsersFragment.
 * Displays users list to an administrator
 *
 * @author Gabriel Salas
 */

@AndroidEntryPoint
class UsersFragment : Fragment(), OnItemClickListener<Int> {

    private lateinit var binding: FragmentUsersBinding
    private val userViewModel: UserViewModel by activityViewModels() // Shared View Model

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentUsersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set Loading GIF image
        Glide.with(this)
            .asGif()
            .load(R.drawable.load)
            .into(binding.usersProgress)

        val recyclerView = binding.usersListView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Set click listeners
        binding.addUserBtn.setOnClickListener {
            findNavController().navigate(R.id.action_usersFragment_to_addUserFragment)
        }

        // Observers
        userViewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                binding.usersTable.visibility = View.INVISIBLE
                binding.usersProgress.visibility = View.VISIBLE
            } else {
                binding.usersTable.visibility = View.VISIBLE
                binding.usersProgress.visibility = View.INVISIBLE
            }
        }

        userViewModel.users.observe(viewLifecycleOwner) { users ->
            if (users.isNotEmpty()) {
                val sortedUsers = users.sortedBy { it.firstName }
                val adapter = UsersAdapter(sortedUsers, this)
                recyclerView.adapter = adapter
            } else {
                recyclerView.adapter = null
            }
        }

        userViewModel.addEditSuccessful.observe(viewLifecycleOwner) { successful ->
            if (successful) {
                userViewModel.getAllUsers() // Update users again
                Toast.makeText(requireContext(), "User Added", Toast.LENGTH_SHORT).show()
            }
        }

        userViewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG).setAnchorView(R.id.bottomNavigationView).show()
                userViewModel.clearError()
            }
        }
    }

    // Get user data when the fragment is resumed or becomes visible
    override fun onResume() {
        super.onResume()
        userViewModel.getAllUsers()
    }

    // Handle clicking user row
    override fun onItemClick(item: Int) {
        val action = UsersFragmentDirections.actionUsersFragmentToProfileFragment(item)
        findNavController().navigate(action)
    }

}