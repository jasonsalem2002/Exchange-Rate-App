package com.kjb04.exchange.login;

import com.kjb04.exchange.Alerts;
import com.kjb04.exchange.Authentication;
import com.kjb04.exchange.OnPageCompleteListener;
import com.kjb04.exchange.PageCompleter;
import com.kjb04.exchange.api.ExchangeService;
import com.kjb04.exchange.api.model.Token;
import com.kjb04.exchange.api.model.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login implements PageCompleter {
    public TextField usernameTextField;
    public PasswordField passwordField;
    private OnPageCompleteListener onPageCompleteListener;

    public void setOnPageCompleteListener(OnPageCompleteListener onPageCompleteListener) {
        this.onPageCompleteListener = onPageCompleteListener;
    }
    public void login(ActionEvent actionEvent) {
        User user = new User(usernameTextField.getText(),
                passwordField.getText());
        ExchangeService.exchangeApi().authenticate(user).enqueue(new
        Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token>
                 response) {
                if (response.isSuccessful()) {
                    Authentication.getInstance().saveToken(response.body().getToken());
                    Authentication.getInstance().saveUsername(usernameTextField.getText());
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Success");
                        alert.setContentText("Logged in successfully.");
                        alert.showAndWait();
                        onPageCompleteListener.onPageCompleted();
                    });
                }
                else {
                    Alerts.showResponse(response);
                }
            }
            @Override
            public void onFailure(Call<Token> call, Throwable throwable) {
                Alerts.connectionFailure();
            }
        });
    }
}