package com.example.currencyexchange.chatting

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyexchange.R
import com.example.currencyexchange.api.Authentication
import com.example.currencyexchange.api.ExchangeService
import com.example.currencyexchange.api.model.Message
import com.example.currencyexchange.data.UserWithLastMessage
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
        val editText: EditText = view.findViewById(R.id.searchperson)
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                usersAdapter.filter.filter(s)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

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
        val intent = Intent(activity, Convo::class.java) //Goes to the chat with this user
        intent.putExtra("username", user.username)
        startActivity(intent)
    }
    private fun fetchchat() {
        myusers.clear()
        Authentication.getToken()?.let { token ->
            ExchangeService.exchangeApi().getmessages("Bearer $token", Authentication.getUsername())
                .enqueue(object : Callback<List<Message>> {
                    override fun onResponse(call: Call<List<Message>>, response: Response<List<Message>>) {
                        val messageList = response.body()
                        //Got all Messages available in database related to my username
                        messageList?.let { fetchUsersAndLastMessages(it) }
                    }
                    override fun onFailure(call: Call<List<Message>>, t: Throwable) {
                        Toast.makeText(context, "Failed to fetch chats.Make sure you are connected to the internet", Toast.LENGTH_LONG).show()
                    }
                })
        }
    }

    private fun fetchUsersAndLastMessages(messages: List<Message>) {
        activity?.runOnUiThread {
            val seenUsers = LinkedHashMap<String, UserWithLastMessage>()
            //for loop in reversed as messages go from oldest to newest so by reversing it we update hashmap with neswest messages whenever we see a recepient/sender other than mine that isnt in hashmap yet
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
                if( seenUsers[username ?: "Unknown"]==null){
                    seenUsers[username ?: "Unknown"] = newMessage
                }

                }
            usersWithLastMessage.clear()
            for (lstmsg in seenUsers.values) {
                usersWithLastMessage.add(lstmsg)
            }
            myusers.addAll(seenUsers.keys)
            Authentication.getUsername()?.let { myusers.add(it) }
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
                        Toast.makeText(context, "Make sure you are connected to the internet.", Toast.LENGTH_LONG).show()
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
