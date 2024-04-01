package com.kjb04.exchange.rates;


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
                 ExchangeRates exchangeRates = response.body();
                 Platform.runLater(() -> {
                     assert exchangeRates != null;
                     buyUsdRateLabel.setText(exchangeRates.lbpToUsd.toString());
                     sellUsdRateLabel.setText(exchangeRates.usdToLbp.toString());
                 });
             }
             @Override
             public void onFailure(Call<ExchangeRates> call, Throwable throwable) {
                 Platform.runLater(() -> {
                     Alert alert = new Alert(Alert.AlertType.ERROR);
                     alert.setTitle("fetchRates Failed");
                     alert.setContentText("Failed to fetch rates.");
                     alert.showAndWait();
                 });
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
                fetchRates();
                Platform.runLater(() -> {
                    usdTextField.setText("");
                    lbpTextField.setText("");
                });
            }

            @Override
            public void onFailure(Call<Object> call, Throwable throwable) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Transaction Failed");
                    alert.setContentText("Failed to add transaction.");
                    alert.showAndWait();
                });
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
