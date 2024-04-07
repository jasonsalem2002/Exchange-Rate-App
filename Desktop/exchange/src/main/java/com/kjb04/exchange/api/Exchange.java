package com.kjb04.exchange.api;

import com.kjb04.exchange.api.model.*;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

import java.util.List;

public interface Exchange {
    @POST("/api/user")
    Call<User> addUser(@Body User user);
    @POST("/api/authentication")
    Call<Token> authenticate(@Body User user);
    @GET("/api/exchangeRate")
    Call<ExchangeRates> getExchangeRates();
    @POST("/api/transaction")
    Call<Object> addTransaction(@Body Transaction transaction,
                                @Header("Authorization") String authorization);
    @GET("/api/transaction")
    Call<List<Transaction>> getTransactions(@Header("Authorization")
                                            String authorization);
    @POST("/api/offer")
    Call<Object> addOffer(@Body Offer offer,
                                @Header("Authorization") String authorization);
}
