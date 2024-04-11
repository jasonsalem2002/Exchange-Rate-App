package com.kjb04.exchange;

import java.util.prefs.Preferences;

public class Authentication {
    private static Authentication instance;
    private static final String TOKEN_KEY = "TOKEN";
    private static final String USERNAME_KEY = "USERNAME";

    private String token;
    private String username;
    private Preferences pref;
    private Authentication() {
        pref = Preferences.userRoot().node(this.getClass().getName());
        token = pref.get(TOKEN_KEY, null);
        username = pref.get(USERNAME_KEY,null);
    }
    static public Authentication getInstance() {
        if (instance == null) {
            instance = new Authentication();
        }
        return instance;
    }
    public String getToken() {
        return token;
    }
    public String getUsername() {
        return username;
    }
    public void saveToken(String token) {
        this.token = token;
        pref.put(TOKEN_KEY, token);
    }
    public void saveUsername(String username) {
        this.username = username;
        pref.put(USERNAME_KEY, username);
    }
    public void deleteToken() {
        this.token = null;
        this.username = null;
        pref.remove(TOKEN_KEY);
        pref.remove(USERNAME_KEY);
    }
}