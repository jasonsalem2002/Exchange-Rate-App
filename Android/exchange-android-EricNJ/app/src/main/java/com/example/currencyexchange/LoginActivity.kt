package com.example.currencyexchange

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.currencyexchange.api.Authentication
import com.example.currencyexchange.api.ExchangeService
import com.example.currencyexchange.api.model.Token
import com.example.currencyexchange.api.model.User
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class LoginActivity : AppCompatActivity() {
    private var usernameEditText: TextInputEditText? = null
    private var passwordEditText: TextInputEditText? = null
    private var loginButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        usernameEditText = findViewById(R.id.username)
        passwordEditText = findViewById(R.id.password)
        loginButton = findViewById(R.id.loginButton)
        loginButton?.setOnClickListener { loginUser() }
    }

    private fun loginUser() {
        val user = User().apply {
            username = usernameEditText?.text.toString()
            password = passwordEditText?.text.toString()
        }

        ExchangeService.exchangeApi().authenticate(user).enqueue(object : Callback<Token> {
            override fun onFailure(call: Call<Token>, t: Throwable) {
                Snackbar.make(loginButton as View, "Login failed.", Snackbar.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<Token>, response: Response<Token>) {
                if (response.isSuccessful) {
                    response.body()?.token?.let { token ->
                        Authentication.saveToken(token)
                        onLoginCompleted()
                    } ?: Snackbar.make(loginButton as View, "Login failed. Please try again.", Snackbar.LENGTH_LONG).show()
                } else {
                    Snackbar.make(loginButton as View, "Invalid credentials.", Snackbar.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun onLoginCompleted() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}