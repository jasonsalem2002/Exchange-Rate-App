package com.kjb04.exchange.trading.tradingCreate;

import com.kjb04.exchange.Authentication;
import com.kjb04.exchange.OnPageCompleteListener;
import com.kjb04.exchange.PageCompleter;
import com.kjb04.exchange.api.ExchangeService;
import com.kjb04.exchange.api.model.Offer;
import com.kjb04.exchange.api.model.Token;
import com.kjb04.exchange.api.model.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TradingCreate implements PageCompleter {
    public ToggleGroup transactionType;
    public TextField tradeAmountTextField;
    public TextField tradeAmountReqTextField;
    public TextField tradeRateTextField;
    private OnPageCompleteListener onPageCompleteListener;
    @Override
    public void setOnPageCompleteListener(OnPageCompleteListener onPageCompleteListener) {
        this.onPageCompleteListener = onPageCompleteListener;
    }

    public void submitOffer(ActionEvent actionEvent) {
        RadioButton radioButton = (RadioButton) transactionType.getSelectedToggle();
        Float tradeAmount = Float.parseFloat(tradeAmountTextField.getText());
        Float tradeAmountReq = Float.parseFloat(tradeAmountReqTextField.getText());
        Offer offer = new Offer(
                tradeAmountReq,
                tradeAmount,
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
