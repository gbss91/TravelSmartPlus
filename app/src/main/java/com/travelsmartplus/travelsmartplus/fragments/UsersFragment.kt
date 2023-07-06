package com.travelsmartplus.travelsmartplus.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.travelsmartplus.travelsmartplus.R
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
class UsersFragment : Fragment() {

    private lateinit var binding: FragmentUsersBinding
    private val userViewModel: UserViewModel by activityViewModels() // Shared View Model

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentUsersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get user data
        userViewModel.getAllUsers()

        val recyclerView = binding.usersListView
        var adapter = UsersAdapter(emptyList())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

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
                adapter = UsersAdapter(users)
                recyclerView.adapter = adapter
            } else {
                recyclerView.adapter = null
            }
        }

        userViewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG).setAnchorView(R.id.bottomNavigationView).show()
                userViewModel.clearError()
            }
        }
    }

    // Get user data again when the fragment is resumed
    override fun onResume() {
        super.onResume()
        userViewModel.getAllUsers()
    }

}