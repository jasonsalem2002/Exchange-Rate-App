package com.kjb04.exchange;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
public class Parent implements Initializable{
    public BorderPane borderPane;
    public Button transactionButton;
    public Button loginButton;
    public Button registerButton;
    public Button logoutButton;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
    public void ratesSelected() {
        swapContent(Section.RATES);
    }
    public void transactionsSelected() {
        swapContent(Section.TRANSACTIONS);
    }
    public void loginSelected() {
        swapContent(Section.LOGIN);
    }
    public void registerSelected() {
        swapContent(Section.REGISTER);
    }
    public void logoutSelected() {
        swapContent(Section.RATES);
    }
    private void swapContent(Section section) {
        try {
            URL url = getClass().getResource(section.getResource());
            FXMLLoader loader = new FXMLLoader(url);
            borderPane.setCenter(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private enum Section {
        RATES,
        TRANSACTIONS,
        LOGIN,
        REGISTER;
        public String getResource() {
            return switch (this) {
                case RATES ->
                        "/com/kjb04/exchange/rates/rates.fxml";
                case TRANSACTIONS ->
                        "/com/kjb04/exchange/rates/rates.fxml";
                case LOGIN ->
                        "/com/kjb04/exchange/rates/rates.fxml";
                case REGISTER ->
                        "/com/kjb04/exchange/rates/rates.fxml";
                default -> null;
            };
        }

    }
}
