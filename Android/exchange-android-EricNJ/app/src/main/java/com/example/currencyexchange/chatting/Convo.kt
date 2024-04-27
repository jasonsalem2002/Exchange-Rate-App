package com.example.currencyexchange.chatting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyexchange.R
import com.example.currencyexchange.api.Authentication
import com.example.currencyexchange.api.ExchangeService
import com.example.currencyexchange.api.model.Message
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Convo : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var userName:String
    private var messages = mutableListOf<Message>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_convo)
        userName = intent.getStringExtra("username") ?: return
        var tt:TextView=findViewById(R.id.usernameTextView)
        tt.text=userName
        val sendButton: Button = findViewById(R.id.sendButton)
        val messageEditText: EditText = findViewById(R.id.messageEditText)

        sendButton.setOnClickListener {
            val messageText = messageEditText.text.toString().trim()
            if (messageText.isNotEmpty()) {
                sendMessage(messageText)
                messageEditText.text.clear()
            } else {
                Toast.makeText(this, "Message cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
        recyclerView = findViewById(R.id.messagesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        messageAdapter = Authentication.getUsername()?.let { MessageAdapter(messages, it) }!!
        recyclerView.adapter = messageAdapter
        val backButton: ImageView = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            finish()  // Closes the current activity and returns to the previous one
        }
        fetchchat()
    }
    private fun fetchchat() {
        Authentication.getToken()?.let { token ->
            val senderUsername = Authentication.getUsername()
            ExchangeService.exchangeApi().getmessages("Bearer $token", senderUsername)
                .enqueue(object : Callback<List<Message>> {
                    override fun onResponse(
                        call: Call<List<Message>>,
                        response: Response<List<Message>>
                    ) {
                        if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                            messages.clear()
                            messages.addAll(response.body()!!.filter {
                                it.senderUsername == userName || it.recepient_Username == userName
                            })
                            messageAdapter.notifyDataSetChanged()
                            scrollToBottom()
                        } else {
                            Toast.makeText(this@Convo, "No messages to display.", Toast.LENGTH_LONG).show()
                            messages.clear()}
                    }

                    override fun onFailure(call: Call<List<Message>>, t: Throwable) {
                        Toast.makeText(
                            this@Convo,
                            "Failed to fetch chat: ${t.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
        }


    }
    private fun scrollToBottom() {
        if (messages.isNotEmpty()) {
            recyclerView.post {
            recyclerView.smoothScrollToPosition(messages.size - 1)
        }}
    }

    private fun sendMessage(s:String){
        val msg = Message().apply {
            this.recepient_Username = userName
            this.senderUsername = Authentication.getUsername()
            this.content= s
        }
        ExchangeService.exchangeApi().addChat(msg, if (Authentication.getToken() != null) "Bearer ${Authentication.getToken()}" else null).enqueue(object : Callback<Any> {

            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    fetchchat()

                } else {
                    Toast.makeText(this@Convo, "Failed to Send chat", Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<Any>, t: Throwable) {
                Toast.makeText(this@Convo, "Could not add chat", Toast.LENGTH_LONG).show()
            }
        })

    }

}