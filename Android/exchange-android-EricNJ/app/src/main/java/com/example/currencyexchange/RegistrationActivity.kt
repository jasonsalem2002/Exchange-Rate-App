package com.example.currencyexchange
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.currencyexchange.api.Authentication
import com.example.currencyexchange.api.ExchangeService
import com.example.currencyexchange.api.model.Token
import com.example.currencyexchange.api.model.User
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrationActivity : AppCompatActivity() {
    private var usernameEditText: TextInputLayout? = null
    private var passwordEditText: TextInputLayout? = null
    private var submitButton: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        usernameEditText = findViewById(R.id.txtInptUsername)
        passwordEditText = findViewById(R.id.txtInptPassword)
        submitButton = findViewById(R.id.btnSubmit)
        submitButton?.setOnClickListener { view ->
            createUser()
        }
    }
    private fun createUser() {
        val user = User()
        user.username = usernameEditText?.editText?.text.toString()
        user.password = passwordEditText?.editText?.text.toString()
        ExchangeService.exchangeApi().addUser(user).enqueue(object :
            Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                Snackbar.make(
                    submitButton as View,
                    "Could not create account.",
                    Snackbar.LENGTH_LONG
                )
                    .show()
            }
            override fun onResponse(call: Call<User>, response:
            Response<User>) {
                if (response.isSuccessful) {
                    ExchangeService.exchangeApi().authenticate(user).enqueue(object :
                        Callback<Token> {
                        override fun onFailure(
                            call: Call<Token>, t:
                            Throwable
                        ) {
                            Toast.makeText(
                                this@RegistrationActivity,
                                "Network Error Chck Internet Connection.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        override fun onResponse(
                            call: Call<Token>, response:
                            Response<Token>
                        ) {

                            Snackbar.make(
                                submitButton as View,
                                "Account Created.",
                                Snackbar.LENGTH_LONG
                            )
                                .show()

                            Authentication.saveUsername(user.username!!)
                            response.body()?.token?.let {
                                Authentication.saveToken(it)
                            }
                            onCompleted()


                        }
                    })


                }

                else{
                    val errorJsonStr = response.errorBody()?.string()
                    val errorMessage = JSONObject(errorJsonStr).getString("error")
                    Toast.makeText(
                        this@RegistrationActivity,
                        errorMessage,
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
        })
    }
    private fun onCompleted() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }
}