package com.kjb04.exchange.register;

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

public class Register implements PageCompleter {
    public TextField usernameTextField;
    public PasswordField passwordField;
    private OnPageCompleteListener onPageCompleteListener;

    public void setOnPageCompleteListener(OnPageCompleteListener onPageCompleteListener) {
        this.onPageCompleteListener = onPageCompleteListener;
    }
    public void register(ActionEvent actionEvent) {
        User user = new User(usernameTextField.getText(),
                passwordField.getText());
        ExchangeService.exchangeApi().addUser(user).enqueue(new
        Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User>
                    response) {
                if (response.isSuccessful()) {
                    ExchangeService.exchangeApi().authenticate(user).enqueue(new
                             Callback<Token>() {
                                 @Override
                                 public void onResponse(Call<Token> call,
                                                        Response<Token> response) {

                                     Authentication.getInstance().saveToken(response.body().getToken());
                                     Authentication.getInstance().saveUsername(usernameTextField.getText());
                                     Platform.runLater(() -> {
                                         Alert alert = new Alert(Alert.AlertType.ERROR);
                                         alert.setTitle("Success");
                                         alert.setContentText("Account created successfully.");
                                         alert.showAndWait();
                                         onPageCompleteListener.onPageCompleted();
                                     });
                                 }

                                 @Override
                                 public void onFailure(Call<Token> call, Throwable
                                         throwable) {
                                     Platform.runLater(() -> {
                                         Alert alert = new Alert(Alert.AlertType.ERROR);
                                         alert.setTitle("Login Failed");
                                         alert.setContentText("Failed to login.");
                                         alert.showAndWait();
                                     });
                                 }
                             });

                }
                else {
                    Alerts.showResponse(response);
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable throwable) {
                Alerts.connectionFailure();
            }
        });
    }
}
