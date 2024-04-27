package com.kjb04.exchange.api.model;

import com.google.gson.annotations.SerializedName;
public class Transaction {
    @SerializedName("usd_amount")
    private Float usdAmount;
    @SerializedName("lbp_amount")
    private Float lbpAmount;
    @SerializedName("usd_to_lbp")
    private Boolean usdToLbp;
    @SerializedName("id")
    private Integer id;
    @SerializedName("added_date")
    private String addedDate;

    public Float getUsdAmount() {
        return usdAmount;
    }

    public Float getLbpAmount() {
        return lbpAmount;
    }

    public Boolean getUsdToLbp() {
        return usdToLbp;
    }

    public Integer getId() {
        return id;
    }

    public String getAddedDate() {
        return addedDate;
    }



    public Transaction(Float usdAmount, Float lbpAmount, Boolean usdToLbp)
    {
        this.usdAmount = usdAmount;
        this.lbpAmount = lbpAmount;
        this.usdToLbp = usdToLbp;
    }
}