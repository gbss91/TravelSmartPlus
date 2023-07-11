package com.travelsmartplus.travelsmartplus.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.travelsmartplus.travelsmartplus.R
import com.travelsmartplus.travelsmartplus.data.models.User

/**
 * UsersAdapter
 * Custom adapter to display users list in [RecyclerView]
 *
 * @author Gabriel Salas
 */

class UsersAdapter(
    private val users: List<User>,
    private val listener: OnItemClickListener<Int>
    ) :
    RecyclerView.Adapter<UsersAdapter.ViewHolder>(), Filterable {

    private var originalUsers: List<User> = users
    private var filteredUsers: List<User> = originalUsers

    // Reference to the type of views
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    OnClickListener{

        val name: TextView = itemView.findViewById(R.id.userRowName)

        // Set OnClick listener
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(filteredUsers[position].id!!)
            }
        }

    }

    // Create new views
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): UsersAdapter.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_user_row, viewGroup, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val user = filteredUsers[position]

        viewHolder.name.text = viewHolder.itemView.context.getString(R.string.users_row_name, user.firstName, user.lastName)
    }

    // Return the size the dataset
    override fun getItemCount() = filteredUsers.size

    // Update adapter data
    fun updateUsers(newUsers: List<User>) {
        originalUsers = newUsers
        filteredUsers = newUsers
        notifyDataSetChanged()
    }

    // Filters data
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()

                // Filter patterns
                val filterPattern = constraint?.toString()?.lowercase()?.trim() ?: ""
                filteredUsers = originalUsers.filter { user ->
                    user.id.toString().contains(filterPattern) ||
                            user.firstName.lowercase().contains(filterPattern) ||
                            user.lastName.lowercase().contains(filterPattern) ||
                            user.email.lowercase().contains(filterPattern)
                }

                filterResults.values = filteredUsers
                filterResults.count = filteredUsers.size
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredUsers = if (results != null) {
                    results.values as List<User>
                } else {
                    originalUsers
                }
                notifyDataSetChanged()
            }
        }
    }

}