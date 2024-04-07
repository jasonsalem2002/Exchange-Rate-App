package com.kjb04.exchange.trading;

import com.kjb04.exchange.Authentication;
import com.kjb04.exchange.OnPageCompleteListener;
import com.kjb04.exchange.PageCompleter;
import com.kjb04.exchange.Parent;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Trading implements Initializable, OnPageCompleteListener {
    public BorderPane borderPane;
    public Button tradingOffersButton;
    public Button tradingCreateButton;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateNavigation();
    }
    @Override
    public void onPageCompleted() {
        swapContent(Trading.Section.OFFERS);
    }
    public void tradingOffersSelected() {
        swapContent(Trading.Section.OFFERS);
    }
    public void tradingCreateSelected() {
        swapContent(Trading.Section.CREATE);
    }

    private void swapContent(Trading.Section section) {
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
        tradingOffersButton.setManaged(true);
        tradingOffersButton.setVisible(true);
        tradingCreateButton.setManaged(true);
        tradingCreateButton.setVisible(true);
    }

    private enum Section {
        OFFERS,
        CREATE;
        public String getResource() {
            return switch (this) {
                case OFFERS ->
                        "/com/kjb04/exchange/trading/tradingOffers/tradingOffers.fxml";
                case CREATE ->
                        "/com/kjb04/exchange/trading/tradingCreate/tradingCreate.fxml";
                default -> null;
            };
        }

        public boolean doesComplete() {
            return switch (this) {
                case CREATE -> true;
                default -> false;
            };
        }

    }
}
