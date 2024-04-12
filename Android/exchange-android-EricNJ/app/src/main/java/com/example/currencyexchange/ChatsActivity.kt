package com.example.currencyexchange

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyexchange.api.Authentication
import com.example.currencyexchange.api.ExchangeService
import com.example.currencyexchange.api.model.Message
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatsActivity : AppCompatActivity(), UsersRVAdapter.OnUserClickListener {
    private lateinit var usersRecyclerView: RecyclerView
    private lateinit var usersAdapter: UsersRVAdapter
    private var usersWithLastMessage: MutableList<UserWithLastMessage> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chats)
        val backButton: ImageView = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }
        val addButton: ImageView = findViewById(R.id.addButton)
        addButton.setOnClickListener {
            fetchUsernamesAndShowDropdown()
        }
        usersRecyclerView = findViewById(R.id.idRVcurrency)
        usersRecyclerView.layoutManager = LinearLayoutManager(this)

        usersAdapter = UsersRVAdapter(usersWithLastMessage,this)
        usersRecyclerView.adapter = usersAdapter

        fetchchat()
    }

    override fun onResume() {
        super.onResume()
        fetchchat()
    }

    override fun onUserClicked(user: UserWithLastMessage) {
        Toast.makeText(this, "Clicked on user: ${user.username}", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, Convo::class.java)
        intent.putExtra("username", user.username)
        startActivity(intent)
    }


    private fun fetchchat() {

        if (Authentication.getToken() != null) {

            val senderUsername = Authentication.getUsername()
            Log.e("Username1",senderUsername.toString())
            val token = Authentication.getToken()
            ExchangeService.exchangeApi().getmessages("Bearer $token", senderUsername)
                .enqueue(object : Callback<List<Message>> {
                    override fun onResponse(call: Call<List<Message>>, response: Response<List<Message>>) {
                        val messageList = response.body()
                        messageList?.let {fetchUsersAndLastMessages(it) }
                    }

                    override fun onFailure(call: Call<List<Message>>, t: Throwable) {
                        Toast.makeText(this@ChatsActivity, "Failed to fetch chat: ${t.message}", Toast.LENGTH_LONG).show()
                    }
                })
        }}
    private fun fetchUsersAndLastMessages(messages: List<Message>) {
        runOnUiThread {
            val seenUsers = HashMap<String, UserWithLastMessage>()

            for (message in messages.reversed()){
                val recipient = message.recepient_Username
                val sender = message.senderUsername
                val currentUsername = Authentication.getUsername()
                val username = when {
                    recipient != currentUsername -> recipient
                    else -> sender
                }
                Log.e("final",username.toString())
                val newMessage = UserWithLastMessage(
                    username = username ?: "Unknown",
                    lastMessage = message.content ?: "No message",
                    timestamp = message.timestamp ?: "Unknown time"

                )
                if(seenUsers[username]==null){
                seenUsers[username ?: "Unknown"] = newMessage
            }}

            usersWithLastMessage.clear()
            usersWithLastMessage.addAll(seenUsers.values)
            usersRecyclerView.adapter?.notifyDataSetChanged()
        }

        usersAdapter = UsersRVAdapter(usersWithLastMessage,this)
        usersRecyclerView.adapter = usersAdapter
    }


    private fun fetchUsernamesAndShowDropdown() {
        Authentication.getToken()?.let { token ->
            ExchangeService.exchangeApi().getusernames("Bearer $token")
                .enqueue(object : Callback<List<String>> {
                    override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                        if (response.isSuccessful) {
                            response.body()?.let { usernames ->
                                showUsernamesDropdown(usernames)
                            }
                        } else {
                            Toast.makeText(this@ChatsActivity, "Failed to fetch usernames.", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<List<String>>, t: Throwable) {
                        Toast.makeText(this@ChatsActivity, "Error fetching usernames: ${t.message}", Toast.LENGTH_LONG).show()
                    }
                })
        }
    }

    private fun showUsernamesDropdown(usernames: List<String>) {
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, usernames)
        AlertDialog.Builder(this)
            .setTitle("Select User")
            .setAdapter(arrayAdapter) { dialog, which ->
                val selectedUser = usernames[which]
                val intent = Intent(this, Convo::class.java).apply {
                    putExtra("username", selectedUser)
                }
                startActivity(intent)
                Toast.makeText(this@ChatsActivity, "Selected: $selectedUser", Toast.LENGTH_SHORT).show()
            }
            .show()
    }
}
