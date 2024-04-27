package com.kjb04.exchange.rates;


import com.kjb04.exchange.Alerts;
import com.kjb04.exchange.Authentication;
import com.kjb04.exchange.api.ExchangeService;
import com.kjb04.exchange.api.model.ExchangeRates;
import com.kjb04.exchange.api.model.Transaction;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import javafx.scene.control.Alert;

import java.util.Objects;


public class Rates {
    public Label buyUsdRateLabel;
    public Label sellUsdRateLabel;
    public TextField lbpTextField;
    public TextField usdTextField;
    public ToggleGroup transactionType;
    public TextField lbpTextFieldConvert;
    public TextField usdTextFieldConvert;
    public ToggleGroup transactionTypeConvert;
    public void initialize() {
        fetchRates();
    }
    private void fetchRates() {
        ExchangeService.exchangeApi().getExchangeRates().enqueue(new
         Callback<ExchangeRates>() {
             @Override
             public void onResponse(Call<ExchangeRates> call,
                                    Response<ExchangeRates> response) {
                 if (response.isSuccessful()) {
                     ExchangeRates exchangeRates = response.body();
                     Platform.runLater(() -> {
                         if (exchangeRates != null) {
                             buyUsdRateLabel.setText(exchangeRates.getLbpToUsd().toString());
                             sellUsdRateLabel.setText(exchangeRates.getUsdToLbp().toString());
                         }
                     });
                 }
                 else {
                     Alerts.showResponse(response);
                 }
             }
             @Override
             public void onFailure(Call<ExchangeRates> call, Throwable throwable) {
                 Alerts.connectionFailure();
             }
         });
    }
    public void addTransaction(ActionEvent actionEvent) {
        float usdAmount = 0;
        float lbpAmount = 0;
        try {
            usdAmount = Float.parseFloat(usdTextField.getText());
            lbpAmount = Float.parseFloat(lbpTextField.getText());
        } catch (NumberFormatException e) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Transaction Failed");
                alert.setContentText("Amounts must be numbers > 0.");
                alert.showAndWait();
            });
            usdTextFieldConvert.setText("");
            lbpTextFieldConvert.setText("");
        }
        RadioButton radioButton = (RadioButton) transactionType.getSelectedToggle();

        if (usdAmount <= 0 || lbpAmount <= 0) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Transaction Failed");
                alert.setContentText("Amounts must be numbers > 0.");
                alert.showAndWait();
            });
            usdTextField.setText("");
            lbpTextField.setText("");
            return;
        }
        if (usdAmount > 1000000000 || lbpAmount > 1000000000) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Transaction Failed");
                alert.setContentText("Number too large. (> 10^15)");
                alert.showAndWait();
            });
            usdTextField.setText("");
            lbpTextField.setText("");
            return;
        }
        if (radioButton == null) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Transaction Failed");
                alert.setContentText("You must select a transaction type.");
                alert.showAndWait();
            });
            usdTextField.setText("");
            lbpTextField.setText("");
            return;
        }

        Transaction transaction = new Transaction(
                usdAmount,
                lbpAmount,
                radioButton.getText().equals("Sell USD")
        );

        String userToken = Authentication.getInstance().getToken();
        String authHeader = userToken != null ? "Bearer " + userToken : null;
        ExchangeService.exchangeApi().addTransaction(transaction,
                authHeader).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object>
                    response) {
                if (response.isSuccessful()) {
                    fetchRates();
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Success");
                        alert.setContentText("Successfully added transaction.");
                        alert.showAndWait();
                        usdTextField.setText("");
                        lbpTextField.setText("");
                    });
                }
                else {
                    Alerts.showResponse(response);
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable throwable) {
                Alerts.connectionFailure();
            }
        });
    }

    public void convert() {
        RadioButton radioButtonConvert = (RadioButton) transactionTypeConvert.getSelectedToggle();
        if (radioButtonConvert == null) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Conversion Failed");
                alert.setContentText("You must select a transaction type.");
                alert.showAndWait();
            });
            usdTextFieldConvert.setText("");
            lbpTextFieldConvert.setText("");
            return;
        }
        else if (radioButtonConvert.getText().equals("LBP to USD")) {
            if (Objects.equals(buyUsdRateLabel.getText(), "Not available")) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Calculator unavailable");
                    alert.setContentText("LBP to USD rate unavailable.");
                    alert.showAndWait();
                });
                return;
            }
            float lbpAmount = 0;
            try {
                lbpAmount = Float.parseFloat(lbpTextFieldConvert.getText());
            } catch (NumberFormatException e) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Conversion Failed");
                    alert.setContentText("Amount must be a number >= 0.");
                    alert.showAndWait();
                });
                usdTextFieldConvert.setText("");
                lbpTextFieldConvert.setText("");
                return;
            }
            if (lbpAmount < 0) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Conversion Failed");
                    alert.setContentText("Amount must be a number >= 0.");
                    alert.showAndWait();
                });
                usdTextFieldConvert.setText("");
                lbpTextFieldConvert.setText("");
                return;
            }
            Float amount = lbpAmount / Float.parseFloat(buyUsdRateLabel.getText());
            usdTextFieldConvert.setText(amount.toString());
        }
        else if (radioButtonConvert.getText().equals("USD to LBP")) {
            if (Objects.equals(sellUsdRateLabel.getText(), "Not available")) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Calculator unavailable");
                    alert.setContentText("USD to LBP rate unavailable.");
                    alert.showAndWait();
                });
                return;
            }
            float usdAmount = 0;
            try {
                usdAmount = Float.parseFloat(usdTextFieldConvert.getText());
            } catch (NumberFormatException e) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Conversion Failed");
                    alert.setContentText("Amount must be a number >= 0.");
                    alert.showAndWait();
                });
                usdTextFieldConvert.setText("");
                lbpTextFieldConvert.setText("");
                return;
            }
            if (usdAmount < 0) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Conversion Failed");
                    alert.setContentText("Amount must be a number >= 0.");
                    alert.showAndWait();
                });
                usdTextFieldConvert.setText("");
                lbpTextFieldConvert.setText("");
                return;
            }
            Float amount = usdAmount * Float.parseFloat(sellUsdRateLabel.getText());
            lbpTextFieldConvert.setText(amount.toString());
        }
    }
}
