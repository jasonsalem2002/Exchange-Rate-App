package com.kjb04.exchange.api;

import com.google.gson.JsonObject;
import com.kjb04.exchange.api.model.*;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;
import java.util.Map;

public interface Exchange {
    @POST("/user")
    Call<User> addUser(@Body User user);
    @POST("/authentication")
    Call<Token> authenticate(@Body User user);
    @GET("/exchangeRate")
    Call<ExchangeRates> getExchangeRates();
    @POST("/transaction")
    Call<Object> addTransaction(@Body Transaction transaction,
                                @Header("Authorization") String authorization);
    @GET("/transaction")
    Call<List<Transaction>> getTransactions(@Header("Authorization")
                                            String authorization);
    @POST("/offers")
    Call<Object> addOffer(@Body Offer offer,
                                @Header("Authorization") String authorization);
    @GET("/offers")
    Call<List<Offer>> getOffers(@Header("Authorization") String authorization);

    @PUT("/accept_offer/{offer_id}")
    Call<Object> acceptOffer(@Header("Authorization") String authorization, @Path("offer_id") Integer offerId);
    @GET("/get_accepted_offers")
    Call<List<Offer>> getAcceptedOffers(@Header("Authorization") String authorization);
    @DELETE("/offers/{offer_id}")
    Call<Object> deleteOffer(@Header("Authorization") String authorization,
                          @Path("offer_id") Integer offerId);

    @POST("/chat")
    Call<Object> sendMessage(@Body Message message,
                          @Header("Authorization") String authorization);
    @GET("/chat/{sender_username}")
    Call<List<Message>> getMessages(@Header("Authorization") String authorization,
                                    @Path("sender_username") String senderUsername);
    @GET("/usernames")
    Call<List<String>> getUsernames(@Header("Authorization") String authorization);
    @POST("/group")
    Call<Object> createGroup(@Body JsonObject group,
                             @Header("Authorization") String authorization);
    @GET("/group/{group_name}/messages")
    Call<List<GroupMessage>> getGroupMessages(@Header("Authorization") String authorization,
                                    @Path("group_name") String groupName);
    @GET("/groups")
    Call<List<String>> getGroupNames(@Header("Authorization") String authorization);
    @GET("/my-groups")
    Call<List<String>> getUserGroups(@Header("Authorization") String authorization);
    @POST("/group/{group_name}/message")
    Call<Object> sendGroupMessage(@Body GroupMessage message,
                             @Header("Authorization") String authorization,
                             @Path("group_name") String groupName);
    @POST("/group/{group_name}/join")
    Call<Object> joinGroup(@Header("Authorization") String authorization,
                                  @Path("group_name") String groupName);
    @POST("/group/{group_name}/leave")
    Call<Object> leaveGroup(@Header("Authorization") String authorization,
                                  @Path("group_name") String groupName);
    @GET("/average_exchange_rate")
    Call<Map<String,Map<String, Float>>> getAvgRates(@Header("Authorization") String authorization,
                                              @Query("start_date") String startDate,
                                              @Query("end_date") String endDate,
                                              @Query("granularity") String granularity
                                     );
    @GET("/predictRate")
    Call<FutureRate> getPredRate(@Header("Authorization") String authorization,
                                                     @Query("date") String date);
    @GET("/next30DaysRates")
    Call<List<FutureRate>> getNext30DaysRates(@Header("Authorization") String authorization);

    @GET("/highest_transaction_today")
    Call<Map<String,Transaction>> getHighestTransactionToday(@Header("Authorization") String authorization);
    @GET("/largest_transaction_amount")
    Call<Map<String,Transaction>> getLargestTransactionAmount(@Header("Authorization") String authorization,
                                                       @Query("start_date") String startDate,
                                                       @Query("end_date") String endDate
    );
    @GET("/number_of_transactions")
    Call<Map<String,Map<String, Float>>> getNumberOfTransactions(@Header("Authorization") String authorization,
                                                     @Query("start_date") String startDate,
                                                     @Query("end_date") String endDate,
                                                     @Query("granularity") String granularity
    );
    @GET("/volume_of_transactions")
    Call<Map<String, Float>> getVolumeOfTransactions(@Header("Authorization") String authorization,
                                                   @Query("start_date") String startDate,
                                                   @Query("end_date") String endDate
    );
}
