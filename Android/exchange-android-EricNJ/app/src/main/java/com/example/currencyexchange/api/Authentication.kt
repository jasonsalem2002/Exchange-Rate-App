package com.example.currencyexchange.api

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

object Authentication {
    private var token: String? = null
    private var preferences: SharedPreferences? = null
    private var username:String?=null
    fun initialize(context: Context) {
        preferences = context.getSharedPreferences("SETTINGS",
            Context.MODE_PRIVATE)
        token = preferences?.getString("TOKEN", null)
        username= preferences?.getString("USERNAME", null)
    }
    fun getToken(): String? {
        return token
    }
    fun saveToken(token: String) {
        this.token = token
        preferences?.edit()?.putString("TOKEN", token)?.apply()
    }
    fun clearToken() {
        this.token = null
        preferences?.edit()?.remove("TOKEN")?.apply()
    }
    fun getUsername(): String? {
        username?.let { Log.e("getus", it) }
        return username
    }
    fun saveUsername(username: String) {
        this.username = username
        preferences?.edit()?.putString("USERNAME",username)?.apply()
    }
    fun clearUsername() {
        this.username = null
        preferences?.edit()?.remove("USERNAME")?.apply()
    }
}
