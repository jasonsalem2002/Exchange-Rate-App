package com.kjb04.exchange.chat;

import com.kjb04.exchange.Authentication;
import com.kjb04.exchange.api.ExchangeService;
import com.kjb04.exchange.api.model.Message;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class Chat implements Initializable {

    public List<Message> messageList;
    public String selectedUsername;

    public TextArea messageBox;
    public ListView preview;
    public ListView chatPane;
    public BorderPane borderPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        fetchChat();

    }


    public void sendMessage() throws IOException {
        Message message = new Message(
                selectedUsername,
                messageBox.getText()
        );

        String userToken = Authentication.getInstance().getToken();
        String authHeader = userToken != null ? "Bearer " + userToken : null;
        if (!messageBox.getText().isEmpty()) {
            ExchangeService.exchangeApi().sendMessage(message,
                    authHeader).enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object>
                        response) {
                    fetchChat();
                    Platform.runLater(() -> {
                        messageBox.setText("");
                        fetchChat();
                    });
                }

                @Override
                public void onFailure(Call<Object> call, Throwable throwable) {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Chat failed");
                        alert.setContentText("Failed to send message.");
                        alert.showAndWait();
                    });
                }
            });
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Chat failed");
            alert.setContentText("Message box empty.");
            alert.showAndWait();
        }
    }

    public void fetchChat() {
        String senderUsername = Authentication.getInstance().getUsername();
        ExchangeService.exchangeApi().getMessages("Bearer " +
                        Authentication.getInstance().getToken(), senderUsername)
                .enqueue(new Callback<List<Message>>() {
                    @Override
                    public void onResponse(Call<List<Message>> call,
                                           Response<List<Message>> response) {
                        messageList = response.body();
                        displayPreview(messageList);
                    }
                    @Override
                    public void onFailure(Call<List<Message>> call,
                                          Throwable throwable) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Chat Failed");
                        alert.setContentText("Failed to fetch chat.");
                        alert.showAndWait();
                    }
                });


    }

    private void displayPreview(List<Message> messages) {
        Platform.runLater(() -> {
            preview.getItems().clear();

            Map<String, HBox> userBoxes = new HashMap<>();

            for (Message msg : messages) {
                String recipient = msg.getRecipientUsername();
                String sender = msg.getSenderUsername();
                String username;
                if (!Objects.equals(recipient, Authentication.getInstance().getUsername())) {
                    if (!Objects.equals(sender, Authentication.getInstance().getUsername())) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Chat Failed");
                        alert.setContentText(recipient+", "+sender+", "+Authentication.getInstance().getUsername());
                        alert.showAndWait();
                        return;
                    }
                    username = recipient;
                }
                else {
                    username = sender;
                }

                HBox hbox = userBoxes.get(username);
                if (hbox == null) {
                    Label label = new Label(username);
                    hbox = new HBox();
                    hbox.getChildren().add(label);
                    hbox.setOnMouseClicked(event -> displayChat(username));
                    userBoxes.put(username, hbox);
                    //userBoxes.put(msg.getContent(), hbox);
                    preview.getItems().add(hbox);
                } else {
                    userBoxes.put(msg.getContent(), hbox);
                }
            }

            displayChat(selectedUsername);
        });
    }

    private void displayChat(String username) {
        Platform.runLater(() -> {
            chatPane.getItems().clear();
            List<Message> filteredMessages = filterChatByUser(username);
            for (Message msg : filteredMessages) {
                Label label = new Label(msg.getContent());

                HBox hbox = new HBox();
                hbox.setMaxWidth(chatPane.getWidth() - 20);
                if (msg.getRecipientUsername().equals(Authentication.getInstance().getUsername())) {
                    hbox.setAlignment(Pos.TOP_LEFT);
                    label.setBackground(new Background(new BackgroundFill(Color.GREEN, null, null)));
                }
                else {
                    hbox.setAlignment(Pos.TOP_RIGHT);
                    label.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));
                }

                hbox.getChildren().add(label);
                chatPane.getItems().add(hbox);
            }
        });
        selectedUsername = username;
    }

    private List<Message> filterChatByUser(String username) {

        return messageList.stream()
                .filter(message -> (message.getSenderUsername().equals(username) | message.getRecipientUsername().equals(username)))
                .toList();
    }



}
