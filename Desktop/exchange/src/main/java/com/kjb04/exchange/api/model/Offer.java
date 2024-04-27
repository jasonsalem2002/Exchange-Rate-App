package com.kjb04.exchange.api.model;

import com.google.gson.annotations.SerializedName;
import com.kjb04.exchange.DateParser;

import java.util.Date;

public class Offer {
    @SerializedName("usd_to_lbp")
    private Boolean usdToLbp;
    @SerializedName("amount_requested")
    private Float amountRequested;
    @SerializedName("amount_to_trade")
    private Float amountToTrade;
    @SerializedName("id")
    private Integer id;
    @SerializedName("username")
    private String username;
    @SerializedName("added_date")
    private String addedDate;

    public Boolean getUsdToLbp() {
        return usdToLbp;
    }
    public Float getAmountToTrade() {
        return amountToTrade;
    }
    public Float getAmountRequested() {
        return amountRequested;
    }
    public String getAddedDate() {
        return addedDate;
    }

    public String getUsername() { return username; }

    public Integer getId() {
        return id;
    }

    public Offer(Float amountRequested, Float amountToTrade, Boolean usdToLbp)
    {
        this.amountRequested = amountRequested;
        this.amountToTrade = amountToTrade;
        this.usdToLbp = usdToLbp;
    }
}
