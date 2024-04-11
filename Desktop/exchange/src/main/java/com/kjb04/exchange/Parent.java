package com.kjb04.exchange;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Parent implements Initializable, OnPageCompleteListener {
    public BorderPane borderPane;
    public Button transactionButton;
    public Button tradingButton;
    public Button chatButton;
    public Button loginButton;
    public Button registerButton;
    public Button logoutButton;

    @Override
    public void onPageCompleted() {
        swapContent(Section.RATES);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateNavigation();
    }
    public void ratesSelected() {
        swapContent(Section.RATES);
    }
    public void transactionsSelected() {
        swapContent(Section.TRANSACTIONS);
    }
    public void tradingSelected() {
        swapContent(Section.TRADING);
    }
    public void chatSelected() {
        swapContent(Section.CHAT);
    }
    public void loginSelected() { swapContent(Section.LOGIN); }
    public void registerSelected() {
        swapContent(Section.REGISTER);
    }
    public void logoutSelected() {
        Authentication.getInstance().deleteToken();
        swapContent(Section.RATES);
    }
    private void swapContent(Section section) {
        try {
            URL url = getClass().getResource(section.getResource());
            FXMLLoader loader = new FXMLLoader(url);
            borderPane.setCenter(loader.load());
            if (section.doesComplete()) {
                ((PageCompleter)
                        loader.getController()).setOnPageCompleteListener(this);
            }
            updateNavigation();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateNavigation() {
        boolean authenticated = Authentication.getInstance().getToken() !=
                null;
        transactionButton.setManaged(authenticated);
        transactionButton.setVisible(authenticated);
        tradingButton.setManaged(authenticated);
        tradingButton.setVisible(authenticated);
        chatButton.setManaged(authenticated);
        chatButton.setVisible(authenticated);
        loginButton.setManaged(!authenticated);
        loginButton.setVisible(!authenticated);
        registerButton.setManaged(!authenticated);
        registerButton.setVisible(!authenticated);
        logoutButton.setManaged(authenticated);
        logoutButton.setVisible(authenticated);
    }


    private enum Section {
        RATES,
        TRANSACTIONS,
        TRADING,
        CHAT,
        LOGIN,
        REGISTER;
        public String getResource() {
            return switch (this) {
                case RATES ->
                        "/com/kjb04/exchange/rates/rates.fxml";
                case TRANSACTIONS ->
                        "/com/kjb04/exchange/transactions/transactions.fxml";
                case TRADING ->
                        "/com/kjb04/exchange/trading/trading.fxml";
                case CHAT ->
                        "/com/kjb04/exchange/chat/chat.fxml";
                case LOGIN ->
                        "/com/kjb04/exchange/login/login.fxml";
                case REGISTER ->
                        "/com/kjb04/exchange/register/register.fxml";
                default -> null;
            };
        }

        public boolean doesComplete() {
            return switch (this) {
                case LOGIN, REGISTER -> true;
                default -> false;
            };
        }


    }

}
