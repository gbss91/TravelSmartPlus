package com.travelsmartplus.travelsmartplus.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
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
    RecyclerView.Adapter<UsersAdapter.ViewHolder>()
{

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
                listener.onItemClick(users[position].id!!)
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
        val user = users[position]

        viewHolder.name.text = viewHolder.itemView.context.getString(R.string.users_row_name, user.firstName, user.lastName)
    }

    // Return the size the dataset
    override fun getItemCount() = users.size

}