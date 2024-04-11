package com.kjb04.exchange.api.model;

import com.google.gson.annotations.SerializedName;

public class Offer {
    @SerializedName("usd_to_lbp")
    public Boolean usdToLbp;
    @SerializedName("amount_requested")
    public Float amountRequested;
    @SerializedName("amount_to_trade")
    public Float amountToTrade;
    @SerializedName("id")
    public Integer id;
    @SerializedName("user_id")
    public Integer userId;
    @SerializedName("added_date")
    public String addedDate;

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
    public Integer getUserId() { return userId; }

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
