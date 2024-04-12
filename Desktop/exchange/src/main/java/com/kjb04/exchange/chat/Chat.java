package com.kjb04.exchange.chat;

import com.kjb04.exchange.Authentication;
import com.kjb04.exchange.api.ExchangeService;
import com.kjb04.exchange.api.model.Group;
import com.kjb04.exchange.api.model.GroupMessage;
import com.kjb04.exchange.api.model.Message;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class Chat implements Initializable {

    public List<Message> messageList;
    public List<String> usernameList;
//    public List<Group> groupList;
//    public List<Group> userGroupList;
//    public List<GroupMessage> groupMessageList;

    public String selectedUsername;
    public ComboBox newChatComboBox;
    public TextArea messageBox;
    public ListView preview;
    public ListView chatPane;
    public BorderPane borderPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fetchChat();
        fetchUsernames();
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
                    Platform.runLater(() -> {
                        messageBox.setText("");
                        fetchUsernames();
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
        String username = Authentication.getInstance().getUsername();
        ExchangeService.exchangeApi().getMessages("Bearer " +
                        Authentication.getInstance().getToken(), username)
                .enqueue(new Callback<List<Message>>() {
                    @Override
                    public void onResponse(Call<List<Message>> call,
                                           Response<List<Message>> response) {
                        messageList = response.body();
                        displayPreview();
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

    public void fetchUsernames() {
        ExchangeService.exchangeApi().getUsernames("Bearer " + Authentication.getInstance().getToken())
                .enqueue(new Callback<List<String>>() {
                    @Override
                    public void onResponse(Call<List<String>> call,
                                           Response<List<String>> response) {
                        usernameList = response.body();
                        initComboBox();
                    }
                    @Override
                    public void onFailure(Call<List<String>> call,
                                          Throwable throwable) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Chat Failed");
                        alert.setContentText("Failed to fetch usernames.");
                        alert.showAndWait();
                    }
                });


    }
    private void initComboBox() {
        List<String> usernames = usernameList;
        Platform.runLater(() -> {
            newChatComboBox.getItems().clear();
            newChatComboBox.getItems().addAll(usernames);
        });
    }

    public void createNewChat() {
        Platform.runLater(() -> {
            if (newChatComboBox.getValue()!=null) {
                selectedUsername = (String) newChatComboBox.getValue();
            }
        });
    }


    private void displayPreview() {
        Platform.runLater(() -> {
            preview.getItems().clear();

            Map<String, VBox> userBoxes = new HashMap<>();

            for (Message msg : messageList) {
                String recipient = msg.getRecipientUsername();
                String sender = msg.getSenderUsername();
                String username;
                if (!Objects.equals(recipient, Authentication.getInstance().getUsername())) {
                    if (!Objects.equals(sender, Authentication.getInstance().getUsername())) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Chat Failed");
                        alert.setContentText("Failed to load chat.");
                        alert.showAndWait();
                        return;
                    }
                    username = recipient;
                }
                else {
                    username = sender;
                }

                VBox hbox = userBoxes.get(username);
                if (hbox == null) {
                    hbox = new VBox();
                    //hbox.setPrefHeight(50);
                    hbox.getChildren().add(new Label(username));
                    hbox.getChildren().add(new Label(msg.getContent()));
                    hbox.getChildren().add(new Label(msg.getAddedDate()));
                    hbox.setOnMouseClicked(event -> displayChat(username));
                    userBoxes.put(username, hbox);
                    preview.getItems().add(hbox);
                } else {
                    hbox.getChildren().get(1).setAccessibleText(msg.getContent()); //check/////////////
                    hbox.getChildren().get(2).setAccessibleText(msg.getAddedDate());
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














//
//    private void fetchGroups() {
//        ExchangeService.exchangeApi().getGroups("Bearer " +
//                        Authentication.getInstance().getToken())
//                .enqueue(new Callback<List<Group>>() {
//                    @Override
//                    public void onResponse(Call<List<Group>> call,
//                                           Response<List<Group>> response) {
//                        groupList = response.body();
//                        //displayPreview();//////////////////
//                    }
//                    @Override
//                    public void onFailure(Call<List<Group>> call,
//                                          Throwable throwable) {
//                        Alert alert = new Alert(Alert.AlertType.ERROR);
//                        alert.setTitle("Groups Failed");
//                        alert.setContentText("Failed to fetch groups.");
//                        alert.showAndWait();
//                    }
//                });
//    }
//
//    private void fetchUserGroups() {
//        String username = Authentication.getInstance().getUsername();
//        ExchangeService.exchangeApi().getUserGroups("Bearer " +
//                        Authentication.getInstance().getToken(), username)
//                .enqueue(new Callback<List<Group>>() {
//                    @Override
//                    public void onResponse(Call<List<Group>> call,
//                                           Response<List<Group>> response) {
//                        userGroupList = response.body();
//                        //displayPreview();//////////////////////////
//                    }
//                    @Override
//                    public void onFailure(Call<List<Group>> call,
//                                          Throwable throwable) {
//                        Alert alert = new Alert(Alert.AlertType.ERROR);
//                        alert.setTitle("Groups Failed");
//                        alert.setContentText("Failed to fetch user groups.");
//                        alert.showAndWait();
//                    }
//                });
//    }
//
//    private void displayGroupsPreview() {
//        Platform.runLater(() -> {
//            groupsPreview.getItems().clear();
//
//            Map<String, HBox> userBoxes = new HashMap<>();
//
//            for (GroupMessage msg : groupMessageList) {
//                String group = msg.getGroupName();
//                String sender = msg.getSenderUsername();
//                String username;
//                if (!Objects.equals(recipient, Authentication.getInstance().getUsername())) {
//                    if (!Objects.equals(sender, Authentication.getInstance().getUsername())) {
//                        Alert alert = new Alert(Alert.AlertType.ERROR);
//                        alert.setTitle("Chat Failed");
//                        alert.setContentText("Failed to load chat.");
//                        alert.showAndWait();
//                        return;
//                    }
//                    username = recipient;
//                }
//                else {
//                    username = sender;
//                }
//
//                HBox hbox = userBoxes.get(username);
//                if (hbox == null) {
//                    hbox = new HBox();
//                    hbox.getChildren().add(new Label(username));
//                    hbox.getChildren().add(new Label(msg.getContent()));
//                    hbox.getChildren().add(new Label(msg.getTimestamp()));
//                    hbox.setOnMouseClicked(event -> displayChat(username));
//                    userBoxes.put(username, hbox);
//                    preview.getItems().add(hbox);
//                } else {
//                    hbox.getChildren().get(1).setAccessibleText(msg.getContent()); //check/////////////
//                    hbox.getChildren().get(2).setAccessibleText(msg.getTimestamp());
//                }
//            }
//
//            displayChat(selectedUsername);
//        });
//    }
//
//    private void displayGroupChat(String username) {
//        Platform.runLater(() -> {
//            chatPane.getItems().clear();
//            List<Message> filteredMessages = filterChatByUser(username);
//            for (Message msg : filteredMessages) {
//                Label label = new Label(msg.getContent());
//
//                HBox hbox = new HBox();
//                hbox.setMaxWidth(chatPane.getWidth() - 20);
//                if (msg.getRecipientUsername().equals(Authentication.getInstance().getUsername())) {
//                    hbox.setAlignment(Pos.TOP_LEFT);
//                    label.setBackground(new Background(new BackgroundFill(Color.GREEN, null, null)));
//                }
//                else {
//                    hbox.setAlignment(Pos.TOP_RIGHT);
//                    label.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));
//                }
//                hbox.getChildren().add(label);
//                chatPane.getItems().add(hbox);
//            }
//        });
//        selectedUsername = username;
//    }
//
//    private List<Message> filterGroupChatByGroup(String username) {
//        return messageList.stream()
//                .filter(message -> (message.getSenderUsername().equals(username) | message.getRecipientUsername().equals(username)))
//                .toList();
//    }


}
