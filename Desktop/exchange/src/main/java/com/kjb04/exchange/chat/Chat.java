package com.kjb04.exchange.chat;

import com.kjb04.exchange.Authentication;
import com.kjb04.exchange.api.ExchangeService;
import com.kjb04.exchange.api.model.GroupMessage;
import com.kjb04.exchange.api.model.Message;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import com.google.gson.JsonObject;

public class Chat implements Initializable {
    public Label chatType;

    public List<Message> messageList;
    public List<String> usernameList;
    public List<String> chatList = new ArrayList<String>();
    public List<String> groupList;
    public List<String> userGroupList;
    public Map<String,List<GroupMessage>> groupMessageMap = new HashMap<String, List<GroupMessage>>();  // Maps Group name to List of all messages on that group

    public String selectedUsername;
    public String selectedGroup;
    public Label chatName;
    public ComboBox newChatComboBox;
    public TextArea messageBox;
    public ListView preview;
    public ListView chatPane;
    public BorderPane borderPane;
    public ScrollPane scrollPane;
    public Button createGroupButton;
    public Button submitGroupButton;
    public Button leaveGroupButton;
    public VBox createGroupBox;
    public TextField createGroupTextField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        chatPane.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
            scrollPane.setVvalue(1.0);  // TODO - Fix
        });
        selectPrivate();
    }

    public void selectPrivate() {
        preview.getItems().clear();
        chatType.setText("Private Chat");
        chatName.setText("");
        leaveGroupButton.setVisible(false);
        createGroupButton.setVisible(false);
        newChatComboBox.setValue("New Chat");
        fetchChat();
        fetchUsernames();
    }

    public void selectGroup() {
        preview.getItems().clear();
        chatType.setText("Groups");
        chatName.setText("");
        leaveGroupButton.setVisible(false);
        createGroupButton.setVisible(true);
        newChatComboBox.setValue("Join Group");
        Platform.runLater(() -> {
            fetchUserGroups();
            fetchGroups();
        });
    }

    public void sendMessage() throws IOException{
        if (chatType.getText().equals("Private Chat")) {
            sendPrivateMessage();
        }
        else if (chatType.getText().equals("Groups")) {
            sendGrpMessage();
        }
    }

    public void sendPrivateMessage() throws IOException {
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
                        Platform.runLater(() -> {
                            usernameList = response.body();
                            initComboBox();
                        });
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
        if (chatType.getText().equals("Private Chat")) {
            List<String> usernames = filterUsernames(usernameList);
            Platform.runLater(() -> {
                newChatComboBox.getItems().clear();
                newChatComboBox.getItems().addAll(usernames);
            });
        }
        else if (chatType.getText().equals("Groups")) {
            if (groupList == null || groupList.isEmpty()) {
                newChatComboBox.getItems().clear();
                return;
            }
            List<String> groups = filterGroups(groupList);
            Platform.runLater(() -> {
                newChatComboBox.getItems().clear();
                newChatComboBox.getItems().addAll(groups);
            });
        }
    }

    public void comboBoxAction() {
        if (chatType.getText().equals("Private Chat")) {
            createNewChat();
        }
        else if (chatType.getText().equals("Groups")) {
            joinGroup();
        }
    }


    private void createNewChat() {
        Platform.runLater(() -> {
            chatPane.getItems().clear();
            if (newChatComboBox.getValue()!=null && usernameList.contains((String)newChatComboBox.getValue())) {
                selectedUsername = (String) newChatComboBox.getValue();
                chatName.setText(selectedUsername);
            }
        });
    }


    private void displayPreview() {
        Platform.runLater(() -> {
            preview.getItems().clear();

            Map<String, VBox> userBoxes = new HashMap<>();

            for (int i = messageList.size()-1; i >=0 ; i--) {
                Message msg = messageList.get(i);
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
                chatList.add(username);

                VBox vbox = userBoxes.get(username);
                if (vbox == null) {
                    vbox = new VBox();
                    //vbox.setPrefHeight(50);
                    vbox.getChildren().add(new Label(username));
                    vbox.getChildren().add(new Label(msg.getContent()));
                    vbox.getChildren().add(new Label(msg.getAddedDate().toString()));
                    vbox.setOnMouseClicked(event -> displayChat(username));
                    userBoxes.put(username, vbox);
                    preview.getItems().add(vbox);
                }
//                else {
//                    vbox.getChildren().get(1).setAccessibleText(msg.getContent()); //check/////////////
//                    vbox.getChildren().get(2).setAccessibleText(msg.getAddedDate());
//                }
            }
            displayChat(selectedUsername);
        });
    }

    private void displayChat(String username) {
        Platform.runLater(() -> {
            chatPane.getItems().clear();
            List<Message> filteredMessages = filterChatByUser(username);
            for (Message msg : filteredMessages) {
                Label contentLabel = new Label(msg.getContent());
                Label timeLabel = new Label(msg.getAddedDate().toString());

                VBox vbox = new VBox();
                vbox.setMaxWidth(chatPane.getWidth() - 20);
                if (msg.getRecipientUsername().equals(Authentication.getInstance().getUsername())) {
                    vbox.setAlignment(Pos.TOP_LEFT);
                    contentLabel.setBackground(new Background(new BackgroundFill(Color.GREEN, null, null)));
                }
                else {
                    vbox.setAlignment(Pos.TOP_RIGHT);
                    contentLabel.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));
                }
                timeLabel.setFont(new Font(8));
                vbox.getChildren().add(contentLabel);
                vbox.getChildren().add(timeLabel);
                chatPane.getItems().add(vbox);
                scrollPane.setVvalue(1.0);
            }
        });
        selectedUsername = username;
        chatName.setText(selectedUsername);
    }

    private List<Message> filterChatByUser(String username) {
        return messageList.stream()
                .filter(message -> (message.getSenderUsername().equals(username) | message.getRecipientUsername().equals(username)))
                .toList();
    }

    private List<String> filterUsernames(List<String> usernames) {
        if (chatList == null || chatList.isEmpty()) {
            return usernames;
        }
        return usernames.stream()
                .filter(username -> (!chatList.contains(username)))
                .toList();
    }
    private List<String> filterGroups(List<String> groups) {
        if (userGroupList == null || userGroupList.isEmpty()) {
            return groups;
        }
        return groups.stream()
                .filter(group -> (!userGroupList.contains(group)))
                .toList();
    }













    public void sendGrpMessage() throws IOException {
        GroupMessage message = new GroupMessage(
                messageBox.getText()
        );

//        Alert alert1 = new Alert(Alert.AlertType.ERROR);
//        alert1.setTitle("TEST ALERT");
//        alert1.setContentText("Sending to Group: "+selectedGroup);
//        alert1.showAndWait();

        String userToken = Authentication.getInstance().getToken();
        String authHeader = userToken != null ? "Bearer " + userToken : null;
        if (!messageBox.getText().isEmpty()) {
            ExchangeService.exchangeApi().sendGroupMessage(message,
                    authHeader,selectedGroup).enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object>
                        response) {
                    Platform.runLater(() -> {
                        messageBox.setText("");
                        refreshGroupChat();
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

    private void refreshGroupChat() {
        groupMessageMap.remove(selectedGroup);
        fetchGroupMessages(selectedGroup);
        while (groupMessageMap.get(selectedGroup) == null) {}
        for (Object o : preview.getItems()) {
            VBox vbox = (VBox)o;
            if (((Label)(vbox.getChildren().getFirst())).getText().equals(selectedGroup)) {
                vbox.getChildren().clear();
                vbox.getChildren().add(new Label(selectedGroup));
                vbox.getChildren().add(new Label(groupMessageMap.get(selectedGroup).getLast().getSenderUsername()
                        +": "+groupMessageMap.get(selectedGroup).getLast().getContent()));
                vbox.getChildren().add(new Label(groupMessageMap.get(selectedGroup).getLast().getAddedDate().toString()));
                preview.getItems().remove(vbox);
                preview.getItems().addFirst(vbox);
                scrollPane.setVvalue(1.0);

                break;
            }
        }
        displayGroupChat(selectedGroup);
    }


    private void fetchGroups() {
        ExchangeService.exchangeApi().getGroupNames("Bearer " +
                        Authentication.getInstance().getToken())
                .enqueue(new Callback<List<String>>() {
                    @Override
                    public void onResponse(Call<List<String>> call,
                                           Response<List<String>> response) {
                        Platform.runLater(() -> {
                                    groupList = response.body();
                                    initComboBox();
                                });
                    }
                    @Override
                    public void onFailure(Call<List<String>> call,
                                          Throwable throwable) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Groups Failed");
                        alert.setContentText("Failed to fetch groups.");
                        alert.showAndWait();
                    }
                });

    }

    private void fetchUserGroups() {
        ExchangeService.exchangeApi().getUserGroups("Bearer " +
                        Authentication.getInstance().getToken())
                .enqueue(new Callback<List<String>>() {
                    @Override
                    public void onResponse(Call<List<String>> call,
                                           Response<List<String>> response) {
                        userGroupList = response.body();
//                        Platform.runLater(() -> {
//                            Alert alert = new Alert(Alert.AlertType.ERROR);
//                            alert.setTitle("TEST ALERT");
//                            alert.setContentText(userGroupList.getFirst());
//                            alert.showAndWait();
//                        });
                        displayGroupsPreview();
                    }
                    @Override
                    public void onFailure(Call<List<String>> call,
                                          Throwable throwable) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Groups Failed");
                        alert.setContentText("Failed to fetch user groups.");
                        alert.showAndWait();
                    }
                });

    }

    private void fetchGroupMessages(String groupName) {
        ExchangeService.exchangeApi().getGroupMessages("Bearer " +
                        Authentication.getInstance().getToken(), groupName)
                .enqueue(new Callback<List<GroupMessage>>() {
                    @Override
                    public void onResponse(Call<List<GroupMessage>> call,
                                           Response<List<GroupMessage>> response) {

                        groupMessageMap.put(groupName,response.body());
//                        Platform.runLater(()->{
//                            Alert alert = new Alert(Alert.AlertType.ERROR);
//                            alert.setTitle("TEST ALERT");
//                            alert.setContentText("Fetched from: "+groupName);
//                            alert.showAndWait();
//                        });
                    }
                    @Override
                    public void onFailure(Call<List<GroupMessage>> call,
                                          Throwable throwable) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Groups Failed");
                        alert.setContentText("Failed to fetch groups.");
                        alert.showAndWait();
                    }
                });
        //return groupMessageMap.get(groupName);
    }

    private void displayGroupsPreview() {
        Platform.runLater(()-> {
            preview.getItems().clear();

            if (userGroupList == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Groups Failed");
                alert.setContentText("nullll");
                alert.showAndWait();
                return;
            }

            for (String groupName : userGroupList) {
                fetchGroupMessages(groupName);
                while (groupMessageMap.get(groupName) == null) {}
            }
            List<String> sortedList = userGroupList
                    .stream()
                    .sorted(Comparator.comparing(e -> (!groupMessageMap.get(e).isEmpty()) ? groupMessageMap.get(e).getLast().getAddedDate() : new Date(0),
                            Comparator.reverseOrder() ))
                    .collect(Collectors.toList());
            userGroupList = sortedList;

            for (String groupName : userGroupList) {
                VBox vbox = new VBox();
                vbox.getChildren().add(new Label(groupName));
                if (groupMessageMap.get(groupName) != null && !groupMessageMap.get(groupName).isEmpty())
                {
                    vbox.getChildren().add(new Label(groupMessageMap.get(groupName).getLast().getSenderUsername()
                            +": "+groupMessageMap.get(groupName).getLast().getContent()));
                    vbox.getChildren().add(new Label(groupMessageMap.get(groupName).getLast().getAddedDate().toString()));
                }
                vbox.setOnMouseClicked(event -> displayGroupChat(groupName));
                //groupBoxes.put(groupName, vbox);
                preview.getItems().add(vbox);

//                Alert alert = new Alert(Alert.AlertType.ERROR);
//                alert.setTitle("TEST ALERT");
//                alert.setContentText("Added to preview: "+groupName);
//                alert.showAndWait();
            }
            displayGroupChat(selectedGroup);
        });
    }

    private void displayGroupChat(String groupName) {
        selectedGroup = groupName;
        chatName.setText(groupName);
        leaveGroupButton.setVisible(true);
        Platform.runLater(() -> {
            chatPane.getItems().clear();
            List<GroupMessage> groupMessages = groupMessageMap.get(groupName);
            if (groupMessages == null) { return; }
            for (GroupMessage msg : groupMessages) {
                Label contentLabel = new Label(msg.getContent());
                Label timeLabel = new Label(msg.getAddedDate().toString());


                VBox vbox = new VBox();
                vbox.setMaxWidth(chatPane.getWidth() - 20);
                if (msg.getSenderUsername().equals(Authentication.getInstance().getUsername())) {
                    vbox.setAlignment(Pos.TOP_RIGHT);
                    contentLabel.setBackground(new Background(new BackgroundFill(Color.GREEN, null, null)));
                }
                else {
                    vbox.setAlignment(Pos.TOP_LEFT);
                    contentLabel.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));
                    Label senderLabel = new Label(msg.getSenderUsername());
                    senderLabel.setFont(new Font(8));
                    vbox.getChildren().add(senderLabel);
                }
                timeLabel.setFont(new Font(8));
                vbox.getChildren().add(contentLabel);
                vbox.getChildren().add(timeLabel);
                chatPane.getItems().add(vbox);
                scrollPane.setVvalue(1.0);
            }
        });

    }

    public void joinGroup() {
        if (newChatComboBox.getValue()==null) {
            return;
        }
        String groupName = (String) newChatComboBox.getValue();

        String userToken = Authentication.getInstance().getToken();
        String authHeader = userToken != null ? "Bearer " + userToken : null;
        ExchangeService.exchangeApi().joinGroup(authHeader, groupName)
                .enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object>
                    response) {
                Platform.runLater(() -> {
//                    selectedGroup = group;
//                    chatName.setText(group);
                    fetchUserGroups();
                    initComboBox();
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


    public void leaveGroup() {
        if (!userGroupList.contains(selectedGroup)) {
            return;
        }

        String userToken = Authentication.getInstance().getToken();
        String authHeader = userToken != null ? "Bearer " + userToken : null;
        ExchangeService.exchangeApi().leaveGroup(authHeader, selectedGroup)
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object>
                            response) {
                        Platform.runLater(() -> {
                            selectGroup();
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

    public void createGroup() {
        String groupName = createGroupTextField.getText();
//        String groupObj = "{\"name\": \"" + groupName + "\"}";
        JsonObject groupObj = new JsonObject();
        groupObj.addProperty("name", groupName);
        String userToken = Authentication.getInstance().getToken();
        String authHeader = userToken != null ? "Bearer " + userToken : null;
        ExchangeService.exchangeApi().createGroup(groupObj, authHeader)
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object>
                            response) {
                        Platform.runLater(() -> {
                            createGroupBox.setVisible(false);
                            selectGroup();
                        });
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable throwable) {
                        Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Chat failed");
                            alert.setContentText("Failed to create group.");
                            alert.showAndWait();
                        });
                    }
                });
    }

    public void revealCreateGroup() {
        createGroupBox.setVisible(true);
    }

}
