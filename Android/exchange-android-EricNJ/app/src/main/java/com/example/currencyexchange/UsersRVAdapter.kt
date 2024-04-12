package com.example.currencyexchange

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UsersRVAdapter(
    private val usersWithMessages: List<UserWithLastMessage>,
    private val clickListener: OnUserClickListener
) : RecyclerView.Adapter<UsersRVAdapter.UserViewHolder>() {
    interface OnUserClickListener {
        fun onUserClicked(user: UserWithLastMessage)
    }

    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val displayNameText: TextView = view.findViewById(R.id.displayNameText)
        private val statusText: TextView = view.findViewById(R.id.statusText)
        private val dateText: TextView = view.findViewById(R.id.dateText)

        fun bind(user: UserWithLastMessage, listener: OnUserClickListener) {
            displayNameText.text = user.username
            statusText.text = user.lastMessage
            dateText.text = user.timestamp
            itemView.setOnClickListener { listener.onUserClicked(user) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_users, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(usersWithMessages[position], clickListener)
    }

    override fun getItemCount(): Int = usersWithMessages.size
}
