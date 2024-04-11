package com.kjb04.exchange.api;

import com.kjb04.exchange.api.model.*;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

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
    @POST("/chat")
    Call<Object> sendMessage(@Body Message message,
                          @Header("Authorization") String authorization);
    @GET("/chat/{sender_username}")
    Call<List<Message>> getMessages(@Header("Authorization") String authorization,
                                    @Path("sender_username") String senderUsername);
}
