package com.example.currencyexchange
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.DecimalFormat
class UsersRVAdapter(private val usersWithMessages: List<UserWithLastMessage>) : RecyclerView.Adapter<UsersRVAdapter.UserViewHolder>() {

    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val displayNameText: TextView = view.findViewById(R.id.displayNameText)
        val statusText: TextView = view.findViewById(R.id.statusText)
        val dateText: TextView = view.findViewById(R.id.dateText)
        // Add other views if necessary
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_users, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = usersWithMessages[position]
        holder.displayNameText.text = user.username
        holder.statusText.text = user.lastMessage
        holder.dateText.text = user.timestamp
    }

    override fun getItemCount(): Int {

        return usersWithMessages.size
    }
}
