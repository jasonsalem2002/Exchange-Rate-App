package com.example.currencyexchange

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyexchange.api.Authentication
import com.example.currencyexchange.api.ExchangeService
import com.example.currencyexchange.api.model.Message
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatsActivity : AppCompatActivity() {
    private lateinit var usersRecyclerView: RecyclerView
    private lateinit var usersAdapter: UsersRVAdapter
    private var usersWithLastMessage: MutableList<UserWithLastMessage> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chats)

        usersRecyclerView = findViewById(R.id.idRVcurrency)
        usersRecyclerView.layoutManager = LinearLayoutManager(this)

        usersAdapter = UsersRVAdapter(usersWithLastMessage)
        usersRecyclerView.adapter = usersAdapter

        fetchchat()
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

            for (message in messages) {
                val recipient = message.recepient_Username
                val sender = message.senderUsername
                Log.e("recepient",message.recepient_Username.toString())
                Log.e("sender",message.senderUsername.toString())
                val currentUsername = Authentication.getUsername()
                Log.e("currentUsername",currentUsername.toString())

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

                seenUsers[username ?: "Unknown"] = newMessage
            }

            usersWithLastMessage.clear()
            usersWithLastMessage.addAll(seenUsers.values)
            usersRecyclerView.adapter?.notifyDataSetChanged()
        }

        usersAdapter = UsersRVAdapter(usersWithLastMessage)
        usersRecyclerView.adapter = usersAdapter}
    }

