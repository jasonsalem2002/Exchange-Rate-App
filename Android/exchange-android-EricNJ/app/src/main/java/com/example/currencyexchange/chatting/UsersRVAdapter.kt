package com.example.currencyexchange.chatting

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.widget.Filter
import android.widget.Filterable
import com.example.currencyexchange.R
import com.example.currencyexchange.data.UserWithLastMessage

class UsersRVAdapter(
    private val usersWithMessages: MutableList<UserWithLastMessage>,
    private val clickListener: OnUserClickListener
) : RecyclerView.Adapter<UsersRVAdapter.UserViewHolder>(), Filterable {

    // Filtered list initialization
    private var usersWithMessagesFiltered: MutableList<UserWithLastMessage> = usersWithMessages

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_users, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(usersWithMessagesFiltered[position], clickListener)
    }

    override fun getItemCount(): Int = usersWithMessagesFiltered.size

    // Implementing the Filterable interface
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                usersWithMessagesFiltered = if (charSearch.isEmpty()) {
                    usersWithMessages
                } else {
                    val resultList = usersWithMessages.filter {
                        it.username.contains(charSearch, ignoreCase = true)
                    }.toMutableList()
                    resultList
                }

                val filterResults = FilterResults()
                filterResults.values = usersWithMessagesFiltered
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                usersWithMessagesFiltered = results?.values as MutableList<UserWithLastMessage>
                notifyDataSetChanged()
            }
        }
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

    interface OnUserClickListener {
        fun onUserClicked(user: UserWithLastMessage)
    }
}

