package com.kjb04.exchange.chat;

import com.kjb04.exchange.Alerts;
import com.kjb04.exchange.Authentication;
import com.kjb04.exchange.api.ExchangeService;
import com.kjb04.exchange.api.model.GroupMessage;
import com.kjb04.exchange.api.model.Message;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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
    public ComboBox joinGroupComboBox;
    public TextField messageBox;
    public ListView preview;
    public ListView chatPane;
    public BorderPane borderPane;
    public ScrollPane scrollPane;
    public Button createGroupButton;
    public Button submitGroupButton;
    public Button leaveGroupButton;
    public VBox createGroupBox;
    public HBox newChatBox;
    public TextField createGroupTextField;
    public TextField newChatTextField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        messageBox.setOnAction(event -> {
            sendMessage();
        });
        selectPrivate();
    }

    public void selectPrivate() {
        preview.getItems().clear();
        chatPane.getItems().clear();
        chatType.setText("Private Chat");
        chatName.setText("");
        newChatBox.setVisible(true);
        leaveGroupButton.setVisible(false);
        createGroupButton.setVisible(false);
        joinGroupComboBox.setVisible(false);
        fetchChat();
        fetchUsernames();
    }

    public void selectGroup() {
        preview.getItems().clear();
        chatPane.getItems().clear();
        chatType.setText("Groups");
        chatName.setText("");
        newChatBox.setVisible(false);
        leaveGroupButton.setVisible(false);
        createGroupButton.setVisible(true);
        joinGroupComboBox.setVisible(true);
        Platform.runLater(() -> {
            fetchUserGroups();
            fetchGroups();
        });
    }

    public void sendMessage() {
        if (chatType.getText().equals("Private Chat")) {
            sendPrivateMessage();
        }
        else if (chatType.getText().equals("Groups")) {
            sendGrpMessage();
        }
    }

    public void sendPrivateMessage() {
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
                        if (response.isSuccessful()) {
                            messageBox.setText("");
                            fetchUsernames();
                            fetchChat();
                        }
                        else {
                            Alerts.showResponse(response);
                        }
                    });
                }

                @Override
                public void onFailure(Call<Object> call, Throwable throwable) {
                    Alerts.connectionFailure();
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
                        if (response.isSuccessful()) {
                            messageList = response.body();
                            displayPreview();
                        }
                        else {
                            Alerts.showResponse(response);
                        }
                    }
                    @Override
                    public void onFailure(Call<List<Message>> call,
                                          Throwable throwable) {
                        Alerts.connectionFailure();
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
                            if (response.isSuccessful()) {
                                usernameList = response.body();
                                initComboBox();
                            }
                            else {
                                Alerts.showResponse(response);
                            }
                        });
                    }
                    @Override
                    public void onFailure(Call<List<String>> call,
                                          Throwable throwable) {
                        Alerts.connectionFailure();
                    }
                });


    }
    private void initComboBox() {
        if (groupList == null || groupList.isEmpty()) {
            joinGroupComboBox.getItems().clear();
            return;
        }
        List<String> groups = filterGroups(groupList);
        Platform.runLater(() -> {
            joinGroupComboBox.getItems().clear();
            joinGroupComboBox.getItems().addAll(groups);
        });
    }


    public void createNewChat() {
        Platform.runLater(() -> {
            chatPane.getItems().clear();
            if (usernameList.contains(newChatTextField.getText())) {
                selectedUsername = newChatTextField.getText();
                chatName.setText(selectedUsername);
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid username");
                alert.setContentText("User "+newChatTextField.getText()+" does not exist.");
                alert.showAndWait();
            }
            newChatTextField.setText("");
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
                    vbox.getChildren().add(new Label(username));
                    String content = msg.getContent().length()>20 ? msg.getContent().substring(0,17)+"..." : msg.getContent();  // Do not display full string if length>20
                    if (content.indexOf('\n')!=-1) { content = content.substring(0,content.indexOf('\n')); }    // Do not display anything after a line break
                    vbox.getChildren().add(new Label(content));
                    vbox.getChildren().add(new Label(msg.getAddedDate().toString()));
                    vbox.setOnMouseClicked(event -> displayChat(username));
                    userBoxes.put(username, vbox);
                    preview.getItems().add(vbox);
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
                Label contentLabel = new Label(msg.getContent());
                Label timeLabel = new Label(msg.getAddedDate().toString());

                VBox vbox = new VBox();
                vbox.setMaxWidth(chatPane.getWidth() - 20);
                if (msg.getRecipientUsername().equals(Authentication.getInstance().getUsername())) {
                    vbox.setAlignment(Pos.TOP_LEFT);
                }
                else {
                    vbox.setAlignment(Pos.TOP_RIGHT);
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





    public void sendGrpMessage() {
        GroupMessage message = new GroupMessage(
                messageBox.getText()
        );
        String userToken = Authentication.getInstance().getToken();
        String authHeader = userToken != null ? "Bearer " + userToken : null;
        if (!messageBox.getText().isEmpty()) {
            ExchangeService.exchangeApi().sendGroupMessage(message,
                    authHeader,selectedGroup).enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object>
                        response) {
                    Platform.runLater(() -> {
                        if (response.isSuccessful()) {
                            messageBox.setText("");
                            refreshGroupChat();
                        }
                        else {
                            Alerts.showResponse(response);
                        }
                    });
                }

                @Override
                public void onFailure(Call<Object> call, Throwable throwable) {
                    Alerts.connectionFailure();
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
                            if (response.isSuccessful()) {
                                groupList = response.body();
                                initComboBox();
                            }
                            else {
                                Alerts.showResponse(response);
                            }
                        });
                    }
                    @Override
                    public void onFailure(Call<List<String>> call,
                                          Throwable throwable) {
                        Alerts.connectionFailure();
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
                        if (response.isSuccessful()) {
                            userGroupList = response.body();
                            displayGroupsPreview();
                        }
                        else {
                            Alerts.showResponse(response);
                        }
                    }
                    @Override
                    public void onFailure(Call<List<String>> call,
                                          Throwable throwable) {
                        Alerts.connectionFailure();
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
                        if (response.isSuccessful()) {
                            groupMessageMap.put(groupName, response.body());
                        }
                        else {
                            Alerts.showResponse(response);
                        }
                    }
                    @Override
                    public void onFailure(Call<List<GroupMessage>> call,
                                          Throwable throwable) {
                        Alerts.connectionFailure();
                    }
                });
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
                preview.getItems().add(vbox);
            }
            leaveGroupButton.setVisible(false);
            if (selectedGroup!=null && !selectedGroup.isEmpty()){
                displayGroupChat(selectedGroup);
            }
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
        if (joinGroupComboBox.getValue()==null) {
            return;
        }
        String groupName = (String) joinGroupComboBox.getValue();

        String userToken = Authentication.getInstance().getToken();
        String authHeader = userToken != null ? "Bearer " + userToken : null;
        ExchangeService.exchangeApi().joinGroup(authHeader, groupName)
                .enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object>
                    response) {
                Platform.runLater(() -> {
                    if (response.isSuccessful()) {
                        fetchUserGroups();
                        initComboBox();
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Success");
                        alert.setContentText("Joined group \""+groupName+"\".");
                        alert.showAndWait();
                    }
                    else {
                        Alerts.showResponse(response);
                    }
                });
            }

            @Override
            public void onFailure(Call<Object> call, Throwable throwable) {
                Alerts.connectionFailure();
            }
        });
    }


    public void leaveGroup() {
        if (!userGroupList.contains(selectedGroup)) {
            return;
        }
        String groupName = selectedGroup;
        String userToken = Authentication.getInstance().getToken();
        String authHeader = userToken != null ? "Bearer " + userToken : null;
        ExchangeService.exchangeApi().leaveGroup(authHeader, groupName)
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object>
                            response) {
                        Platform.runLater(() -> {
                            if (response.isSuccessful()) {
                                selectGroup();
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Success");
                                alert.setContentText("Left group \""+groupName+"\".");
                                alert.showAndWait();
                            }
                            else {
                                Alerts.showResponse(response);
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable throwable) {
                        Alerts.connectionFailure();
                    }
                });
    }

    public void createGroup() {
        String groupName = createGroupTextField.getText();
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
                            if (response.isSuccessful()) {
                                createGroupBox.setVisible(false);
                                selectGroup();
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Success");
                                alert.setContentText("Created group \""+groupName+"\".");
                                alert.showAndWait();
                            }
                            else {
                                Alerts.showResponse(response);
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable throwable) {
                        Alerts.connectionFailure();
                    }
                });
    }

    public void revealCreateGroup() {
        createGroupBox.setVisible(!createGroupBox.isVisible());
    }

}
