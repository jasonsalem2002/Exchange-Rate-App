package com.kjb04.exchange;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import retrofit2.Response;
import java.io.IOException;

public class Alerts {
    public static void showResponse(Response<?> response) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            if (response.isSuccessful()) {
                alert.setAlertType(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setContentText("Operation was successful.");
            } else {
                alert.setTitle("Error Occurred");
                String errorMessage = "An error occurred.";
                if (response.errorBody() != null) {
                    try {
                        String jsonStr = response.errorBody().string();
                        JsonParser parser = new JsonParser();
                        JsonElement jsonTree = parser.parse(jsonStr);
                        if (jsonTree.isJsonObject()) {
                            JsonObject jsonObject = jsonTree.getAsJsonObject();
                            if (jsonObject.has("error")) {
                                errorMessage = jsonObject.get("error").getAsString();
                            }
                        }
                    } catch (IOException e) {
                        errorMessage = "Failed to parse the error message.";
                    }
                }
                alert.setContentText(errorMessage);
            }
            alert.showAndWait();
        });
    }
    public static void connectionFailure() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Connection Error");
            alert.setContentText("Please connect to the Internet");
            alert.showAndWait();
        });
    }
}
