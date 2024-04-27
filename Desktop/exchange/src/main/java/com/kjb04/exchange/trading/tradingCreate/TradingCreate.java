package com.kjb04.exchange.trading.tradingCreate;

import com.kjb04.exchange.Alerts;
import com.kjb04.exchange.Authentication;
import com.kjb04.exchange.OnPageCompleteListener;
import com.kjb04.exchange.PageCompleter;
import com.kjb04.exchange.api.ExchangeService;
import com.kjb04.exchange.api.model.Offer;
import com.kjb04.exchange.api.model.Token;
import com.kjb04.exchange.api.model.User;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TradingCreate implements PageCompleter {
    public ToggleGroup transactionType;
    public TextField tradeAmountTextField;
    public TextField tradeAmountReqTextField;
    public Label tradeRateTextField;
    private OnPageCompleteListener onPageCompleteListener;
    public Label tradeAmountReqLabel;
    public Label tradeAmountLabel;

    @Override
    public void setOnPageCompleteListener(OnPageCompleteListener onPageCompleteListener) {
        this.onPageCompleteListener = onPageCompleteListener;
    }

    public void submitOffer(ActionEvent actionEvent) {
        RadioButton radioButton = (RadioButton) transactionType.getSelectedToggle();
        Float amountOffered;
        Float amountRequested;
        try {
            amountOffered = Float.parseFloat(tradeAmountTextField.getText());
            amountRequested = Float.parseFloat(tradeAmountReqTextField.getText());
        } catch (NumberFormatException e) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Conversion Failed");
                alert.setContentText("Amount must be a number >= 0.");
                alert.showAndWait();
            });
            tradeAmountTextField.setText("");
            tradeAmountReqTextField.setText("");
            return;
        }

        Offer offer = new Offer(
                amountRequested,
                amountOffered,
                radioButton.getText().equals("Sell USD")
        );
        String userToken = Authentication.getInstance().getToken();
        String authHeader = userToken != null ? "Bearer " + userToken : null;
        ExchangeService.exchangeApi().addOffer(offer,
                authHeader).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object>
                    response) {
                Platform.runLater(() -> {
                    onPageCompleteListener.onPageCompleted();
                });
            }

            @Override
            public void onFailure(Call<Object> call, Throwable throwable) {
                Alerts.connectionFailure();
            }
        });

    }

    public void buySelected() {
        tradeAmountReqLabel.setText("Amount Requested (USD)");
        tradeAmountLabel.setText("Amount to Trade (LBP)");
        displayRate();
    }
    public void sellSelected() {
        tradeAmountReqLabel.setText("Amount Requested (LBP)");
        tradeAmountLabel.setText("Amount to Trade (USD)");
        displayRate();
    }

    public void displayRate() {
        if (tradeAmountTextField.getText() == null || tradeAmountTextField.getText().isEmpty()
                || tradeAmountReqTextField.getText() == null || tradeAmountReqTextField.getText().isEmpty()) {
            return;
        }
        Float amountOffered;
        Float amountRequested;
        try {
            amountOffered = Float.parseFloat(tradeAmountTextField.getText());
            amountRequested = Float.parseFloat(tradeAmountReqTextField.getText());
        } catch (NumberFormatException e) {
            return;
        }
        Float rate;
        RadioButton radioButton = (RadioButton) transactionType.getSelectedToggle();
        if (radioButton.getText().equals("Sell USD")) {
            rate = amountRequested / amountOffered;
        }
        else {
            rate = amountOffered / amountRequested;
        }
        tradeRateTextField.setText(rate.toString());
    }
}
