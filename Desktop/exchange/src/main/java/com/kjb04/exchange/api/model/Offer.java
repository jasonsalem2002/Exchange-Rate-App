package com.kjb04.exchange.api.model;

import com.google.gson.annotations.SerializedName;
import com.kjb04.exchange.DateParser;

import java.util.Date;

public class Offer {
    @SerializedName("usd_to_lbp")
    public Boolean usdToLbp;
    @SerializedName("amount_requested")
    public Float amountRequested;
    @SerializedName("amount_to_trade")
    public Float amountToTrade;
    @SerializedName("id")
    public Integer id;
    @SerializedName("username")
    public String username;
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
    public Date getAddedDate() {
        return DateParser.convertStringToDate(addedDate);
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
