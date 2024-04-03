package com.example.currencyexchange.api

import com.example.currencyexchange.api.model.ExchangeRates
import com.example.currencyexchange.api.model.Token
import com.example.currencyexchange.api.model.Transaction
import com.example.currencyexchange.api.model.User
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST


object ExchangeService {
    private const val API_URL: String = "http://f05c-212-36-194-11.ngrok-free.app/"
    fun exchangeApi(): Exchange {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(Exchange::class.java);
    }

    interface Exchange {
        @GET("exchangeRate")
        fun getExchangeRates(): Call<ExchangeRates>

        @POST("/transaction")
        fun addTransaction(@Body transaction: Transaction,
                           @Header("Authorization") authorization: String?): Call<Any>
        @GET("/transaction")
        fun getTransactions(@Header("Authorization") authorization: String):
                Call<List<Transaction>>

        @POST("/user")
        fun addUser(@Body user: User): Call<User>

        @POST("/authentication")
        fun authenticate(@Body user: User): Call<Token>
    }
}