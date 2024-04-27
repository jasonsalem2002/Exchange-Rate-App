package com.kjb04.exchange.api.model;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id")
    private Integer id;
    @SerializedName("user_name")
    private String username;
    @SerializedName("password")
    private String password;

    public String getUsername() { return username; }
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
