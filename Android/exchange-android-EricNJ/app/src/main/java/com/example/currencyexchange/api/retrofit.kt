package com.example.currencyexchange.api

import com.example.currencyexchange.api.model.ExchangeRates
import com.example.currencyexchange.api.model.Message
import com.example.currencyexchange.api.model.Offer
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
import retrofit2.http.PUT
import retrofit2.http.Path


object ExchangeService {
    private const val API_URL: String = "https://scorpion-glowing-guppy.ngrok-free.app/"
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

        @POST("transaction")
        fun addTransaction(@Body transaction: Transaction,
                           @Header("Authorization") authorization: String?): Call<Any>
        @GET("transaction")
        fun getTransactions(@Header("Authorization") authorization: String):
                Call<List<Transaction>>
        @POST("user")
        fun addUser(@Body user: User): Call<User>

        @POST("authentication")
        fun authenticate(@Body user: User): Call<Token>
        @GET("offers")
        fun getOffers(@Header("Authorization") authorization: String):
                Call<List<Offer>>
        @POST("offers")
        fun addoffer(@Body offer: Offer, @Header("Authorization") authorization: String?): Call<Any>

        @PUT("accept_offer/{offer_id}")
        fun acceptOffer(@Path("offer_id") offerId: Int, @Header("Authorization") authorization: String?): Call<Any>
        @GET("usernames")
        fun getusernames(@Header("Authorization") authorization: String): Call<List<String>>
        @GET("chat/{sender_username}")
        fun getmessages(@Header("Authorization") authorization: String,@Path("sender_username") senderUsername:String?): Call<List<Message>>

    }
}