package com.kjb04.exchange.rates;


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
        float usdAmount = Float.parseFloat(usdTextField.getText());
        float lbpAmount = Float.parseFloat(lbpTextField.getText());
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
        if (radioButton == null ) {
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


        ExchangeService.exchangeApi().addTransaction(transaction).enqueue(new
            Callback<Object>() {
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
                public void onFailure(Call<Object> call, Throwable throwable)
                {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Transaction Failed");
                        alert.setContentText("Failed to add transaction.");
                        alert.showAndWait();
                    });
                }
            });
    }

}
