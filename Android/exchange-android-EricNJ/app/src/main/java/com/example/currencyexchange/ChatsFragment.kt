package com.example.currencyexchange

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyexchange.api.Authentication
import com.example.currencyexchange.api.ExchangeService
import com.example.currencyexchange.api.model.Message
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatsFragment : Fragment(), UsersRVAdapter.OnUserClickListener {
    private lateinit var usersRecyclerView: RecyclerView
    private lateinit var usersAdapter: UsersRVAdapter
    private var usersWithLastMessage: MutableList<UserWithLastMessage> = mutableListOf()
    private var myusers:MutableList<String> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chats, container, false)

        val backButton: ImageView = view.findViewById(R.id.backButton)
        backButton.setOnClickListener {
            requireActivity().finish()
        }

        val addButton: ImageView = view.findViewById(R.id.addButton)
        addButton.setOnClickListener {
            fetchUsernamesAndShowDropdown()
        }

        usersRecyclerView = view.findViewById(R.id.idRVcurrency)
        usersRecyclerView.layoutManager = LinearLayoutManager(context)

        usersAdapter = UsersRVAdapter(usersWithLastMessage, this)
        usersRecyclerView.adapter = usersAdapter

        fetchchat()
        return view
    }

    override fun onResume() {
        super.onResume()
        fetchchat()
    }

    override fun onUserClicked(user: UserWithLastMessage) {
        Toast.makeText(context, "Clicked on user: ${user.username}", Toast.LENGTH_SHORT).show()
        val intent = Intent(activity, Convo::class.java)
        intent.putExtra("username", user.username)
        startActivity(intent)
    }

    private fun fetchchat() {
        myusers.clear()
        Authentication.getToken()?.let { token ->
            val senderUsername = Authentication.getUsername()
            ExchangeService.exchangeApi().getmessages("Bearer $token", senderUsername)
                .enqueue(object : Callback<List<Message>> {
                    override fun onResponse(call: Call<List<Message>>, response: Response<List<Message>>) {
                        val messageList = response.body()
                        messageList?.let { fetchUsersAndLastMessages(it) }
                    }

                    override fun onFailure(call: Call<List<Message>>, t: Throwable) {
                        Toast.makeText(context, "Failed to fetch chat: ${t.message}", Toast.LENGTH_LONG).show()
                    }
                })
        }
    }

    private fun fetchUsersAndLastMessages(messages: List<Message>) {
        activity?.runOnUiThread {
            val seenUsers = HashMap<String, UserWithLastMessage>()
            for (message in messages.reversed()) {
                val recipient = message.recepient_Username
                val sender = message.senderUsername
                val currentUsername = Authentication.getUsername()
                val username = when {
                    recipient != currentUsername -> recipient
                    else -> sender
                }
                val newMessage = UserWithLastMessage(
                    username = username ?: "Unknown",
                    lastMessage = message.content ?: "No message",
                    timestamp = message.timestamp ?: "Unknown time"
                )
                myusers.add(newMessage.username)
                if(seenUsers[username]==null){
                    seenUsers[username ?: "Unknown"] = newMessage
                }}

            usersWithLastMessage.clear()
            usersWithLastMessage.addAll(seenUsers.values)
            usersRecyclerView.adapter?.notifyDataSetChanged()
        }
    }

    private fun fetchUsernamesAndShowDropdown() {
        Authentication.getToken()?.let { token ->
            ExchangeService.exchangeApi().getusernames("Bearer $token")
                .enqueue(object : Callback<List<String>> {
                    override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                        if (response.isSuccessful) {
                            response.body()?.let { usernames ->
                                val filteredUsernames = usernames.filterNot { myusers.contains(it) }
                                showUsernamesDropdown(filteredUsernames)
                            }
                        } else {
                            Toast.makeText(context, "Failed to fetch usernames.", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<List<String>>, t: Throwable) {
                        Toast.makeText(context, "Error fetching usernames: ${t.message}", Toast.LENGTH_LONG).show()
                    }
                })
        }
    }

    private fun showUsernamesDropdown(usernames: List<String>) {
        val arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, usernames)
        AlertDialog.Builder(requireContext())
            .setTitle("Chat with a User")
            .setAdapter(arrayAdapter) { dialog, which ->
                val selectedUser = usernames[which]
                val intent = Intent(activity, Convo::class.java).apply {
                    putExtra("username", selectedUser)
                }
                startActivity(intent)

            }
            .show()
    }
}
