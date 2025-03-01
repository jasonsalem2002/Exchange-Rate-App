package com.example.currencyexchange.api

import com.example.currencyexchange.api.model.ExchangeRates
import com.example.currencyexchange.api.model.Group
import com.example.currencyexchange.api.model.GroupMessage
import com.example.currencyexchange.api.model.Message
import com.example.currencyexchange.api.model.Offer
import com.example.currencyexchange.api.model.Token
import com.example.currencyexchange.api.model.Transaction
import com.example.currencyexchange.api.model.User
import com.example.currencyexchange.data.FutureRate
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query


object ExchangeService {
    private const val API_URL: String = "https://salex.hydra-polaris.ts.net/"
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
        fun addTransaction(
            @Body transaction: Transaction,
            @Header("Authorization") authorization: String?
        ): Call<Any>

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

        @GET("get_accepted_offers")
        fun getacceptedoffers(@Header("Authorization") authorization: String?): Call<List<Offer>>

        @PUT("accept_offer/{offer_id}")
        fun acceptOffer(
            @Path("offer_id") offerId: Int,
            @Header("Authorization") authorization: String?
        ): Call<Any>
        @DELETE("offers/{offer_id}")
        fun deleteOffer(
            @Path("offer_id") offerId: Int,
            @Header("Authorization") authorization: String?
        ): Call<Any>

        @GET("usernames")
        fun getusernames(@Header("Authorization") authorization: String): Call<List<String>>

        @GET("chat/{sender_username}")
        fun getmessages(
            @Header("Authorization") authorization: String,
            @Path("sender_username") senderUsername: String?
        ): Call<List<Message>>

        @POST("chat")
        fun addChat(
            @Body message: Message,
            @Header("Authorization") authorization: String?
        ): Call<Any>

        @POST("group")
        fun createGroup(
            @Body groupName: Group?,
            @Header("Authorization") authorization: String?
        ): Call<Any>

        @GET("group/{group_name}/messages")
        fun getGroupMessages(
            @Header("Authorization") authorization: String?,
            @Path("group_name") groupName: String?
        ): Call<List<GroupMessage>>

        @GET("groups")
        fun getGroupNames(@Header("Authorization") authorization: String): Call<List<String>>

        @GET("my-groups")
        fun getMyGroupNames(@Header("Authorization") authorization: String): Call<List<String>>

        @POST("group/{group_name}/message")
        fun sendGroupMessage(
            @Body message: GroupMessage?,
            @Header("Authorization") authorization: String?,
            @Path("group_name") groupName: String?
        ): Call<Any>

        @POST("group/{group_name}/join")
        fun joinGroup(
            @Header("Authorization") authorization: String?,
            @Path("group_name") groupName: String?
        ): Call<Any>

        @POST("group/{group_name}/leave")
        fun leaveGroup(
            @Header("Authorization") authorization: String?,
            @Path("group_name") groupName: String?
        ): Call<Any>

        @GET("/average_exchange_rate")
        fun getAvgRates(
            @Header("Authorization") authorization: String?,
            @Query("start_date") startDate: String?,
            @Query("end_date") endDate: String?,
            @Query("granularity") granularity: String?
        ): Call<Map<String, Map<String, Double>>>

        @GET("/predictRate")
        fun getPredRate(
            @Header("Authorization") authorization: String?,
            @Query("date") date: String?
        ): Call<FutureRate>

        @GET("/next30DaysRates")
        fun getNext30DaysRates(@Header("Authorization") authorization: String?): Call<List<FutureRate>>


        @GET("/highest_transaction_today")
        fun getHighestTransactionToday(@Header("Authorization") authorization: String?): Call<Map<String?, Transaction>>

        @GET("/largest_transaction_amount")
        fun getLargestTransactionAmount(
            @Header("Authorization") authorization: String?,
            @Query("start_date") startDate: String?,
            @Query("end_date") endDate: String?
        ): Call<Map<String?, Transaction>>

        @GET("/number_of_transactions")
        fun getNumberOfTransactions(
            @Header("Authorization") authorization: String?,
            @Query("start_date") startDate: String?,
            @Query("end_date") endDate: String?,
            @Query("granularity") granularity: String?
        ): Call<Map<String, Map<String,Double>>>
        @GET("/volume_of_transactions")
        fun getVolumeOfTransactions(
            @Header("Authorization") authorization: String?,
            @Query("start_date") startDate: String?,
            @Query("end_date") endDate: String?
        ): Call<Map<String?, Float>>

    }


}