package com.kjb04.exchange.statistics;

import com.kjb04.exchange.Alerts;
import com.kjb04.exchange.Authentication;
import com.kjb04.exchange.DateParser;
import com.kjb04.exchange.api.ExchangeService;
import com.kjb04.exchange.api.model.Message;
import com.kjb04.exchange.api.model.Transaction;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Statistics {
    public LineChart lineChart;
    public DatePicker startDatePicker;
    public DatePicker endDatePicker;
    public DatePicker startDateLTPicker;
    public DatePicker endDateLTPicker;
    public DatePicker startDateTVPicker;
    public DatePicker endDateTVPicker;
    public ComboBox<String> granularityComboBox;
    public Label granNoteLabel;
    public Label highestTransactionTodayLabel;
    public Label largestTransactionLabel1;
    public Label largestTransactionLabel2;
    public Label transactionVolumeLabel1;
    public Label transactionVolumeLabel2;
    public NumberAxis yAxis;

    public void initialize() {
        granularityComboBox.getItems().addAll("daily","weekly","monthly","yearly");
        fetchHighestTransactionToday();
    }
    public void graph() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        String startDate = startDatePicker.getValue().toString();
        if (startDate.compareTo(LocalDate.now().toString()) > 0) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid input");
                alert.setContentText("Cannot input start date in the future.");
                alert.showAndWait();
                startDateTVPicker.setValue(null);
            });
            return;
        }
        String endDate = endDatePicker.getValue().toString();
        if (startDate.compareTo(endDate) > -1) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid input");
                alert.setContentText("Start date should be before end date.");
                alert.showAndWait();
            });
            return;
        }
        String granularity = granularityComboBox.getValue();


        ExchangeService.exchangeApi().getNumberOfTransactions("Bearer " + Authentication.getInstance().getToken(),
                        startDate, endDate, granularity
                )
                .enqueue(new Callback<Map<String, Map<String, Float>>>() {
                    @Override
                    public void onResponse(Call<Map<String, Map<String, Float>>> call,
                                           Response<Map<String, Map<String, Float>>> response) {
                        Platform.runLater(() -> {
                            if (response.isSuccessful()) {
                                lineChart.getData().clear();
                                startDatePicker.setValue(null);
                                endDatePicker.setValue(null);
                                Map<String, Map<String, Float>> data = response.body();
                                lineChart.setTitle(granularity == null ? "daily" : granularity + " number of transactions, " + startDate + " to " + endDate);

                                if (data == null) {
                                    Platform.runLater(() -> {
                                        Alert alert = new Alert(Alert.AlertType.ERROR);
                                        alert.setTitle("Data Unavailable");
                                        alert.setContentText("This data is not available at this time.");
                                        alert.showAndWait();
                                    });
                                } else {
                                    if (data.get("transactions_per_period") != null && !data.get("transactions_per_period").isEmpty()) {
                                        for (String x : data.get("transactions_per_period").keySet()) {
                                            series.getData().add(new XYChart.Data<>(x, Math.round(data.get("transactions_per_period").get(x) * 100) / 100.0f));
                                        }
                                        lineChart.getData().add(series);

                                        Float min = Collections.min(data.get("transactions_per_period").values());
                                        Float max = Collections.max(data.get("transactions_per_period").values());
                                        yAxis.setTickUnit((max + min + (max - min) / 2) / 2000);
                                        yAxis.setLowerBound(min - (max - min) / 4);
                                        yAxis.setUpperBound(max + (max - min) / 4);
                                    }
                                }
                            }
                            else {
                                Alerts.showResponse(response);
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<Map<String, Map<String, Float>>> call,
                                          Throwable throwable) {
                        Alerts.connectionFailure();
                    }
                });
    }

    public void displayGranNote() {
        if (granularityComboBox.getValue().equals("daily")) {
            granNoteLabel.setText("Daily granularity: only up to 60 days of data.");
        }
        else if (granularityComboBox.getValue().equals("weekly")) {
            granNoteLabel.setText("Weekly granularity: only up to 52 weeks of data.");
        }
        else if (granularityComboBox.getValue().equals("monthly")) {
            granNoteLabel.setText("Monthly granularity: only up to 36 months of data.");
        }
        else {
            granNoteLabel.setText("");
        }
    }

    public void fetchHighestTransactionToday() {
        ExchangeService.exchangeApi().getHighestTransactionToday("Bearer " + Authentication.getInstance().getToken())
                .enqueue(new Callback<Map<String, Transaction>>() {
                    @Override
                    public void onResponse(Call<Map<String, Transaction>> call,
                                           Response<Map<String, Transaction>> response) {
                        Platform.runLater(() -> {
                            if (response.isSuccessful()) {
                                Transaction transaction = response.body().get("highest_transaction");
                                if (transaction.getUsdToLbp()){
                                    highestTransactionTodayLabel.setText(transaction.getUsdAmount().toString()+" USD -> "+transaction.getLbpAmount().toString()+" LBP");
                                }
                                else {
                                    highestTransactionTodayLabel.setText(transaction.getLbpAmount().toString()+" LBP -> "+transaction.getUsdAmount().toString()+" USD");
                                }
                            }
                            else {
                                Alerts.showResponse(response);
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<Map<String, Transaction>> call,
                                          Throwable throwable) {
                        Alerts.connectionFailure();
                    }
                });



    }
    public void fetchLargestTransaction() {
        String startDate;
        String endDate;
        try {
            startDate = startDateLTPicker.getValue().toString();
            if (startDate.compareTo(LocalDate.now().toString()) > 0) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid input");
                    alert.setContentText("Cannot input start date in the future.");
                    alert.showAndWait();
                    startDateTVPicker.setValue(null);
                });
                return;
            }
            endDate = endDateLTPicker.getValue().toString();
            if (startDate.compareTo(endDate) > -1) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid input");
                    alert.setContentText("Start date should be before end date.");
                    alert.showAndWait();
                });
                return;
            }
        } catch (NullPointerException e) {
            return;
        }

        ExchangeService.exchangeApi().getLargestTransactionAmount("Bearer " + Authentication.getInstance().getToken(),
                        startDate, endDate
                )
                .enqueue(new Callback<Map<String, Transaction>>() {
                    @Override
                    public void onResponse(Call<Map<String, Transaction>> call,
                                           Response<Map<String, Transaction>> response) {
                        Platform.runLater(() -> {
                            if (response.isSuccessful()) {
                                Transaction transaction = response.body().get("largest_transaction");
                                if (transaction.getUsdToLbp()) {
                                    largestTransactionLabel1.setText(transaction.getUsdAmount().toString() + " USD -> " + transaction.getLbpAmount().toString() + " LBP");
                                } else {
                                    largestTransactionLabel1.setText(transaction.getLbpAmount().toString() + " LBP -> " + transaction.getUsdAmount().toString() + " USD");
                                }
                                largestTransactionLabel2.setText("Rate: " + transaction.getLbpAmount() / transaction.getUsdAmount());
                            }
                            else {
                                Alerts.showResponse(response);
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<Map<String, Transaction>> call,
                                          Throwable throwable) {
                        Alerts.connectionFailure();
                    }
                });
    }

    public void fetchTransactionVolume() {
        String startDate;
        String endDate;
        try {
            startDate = startDateTVPicker.getValue().toString();
            if (startDate.compareTo(LocalDate.now().toString()) > 0) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid input");
                    alert.setContentText("Cannot input start date in the future.");
                    alert.showAndWait();
                    startDateTVPicker.setValue(null);
                });
                return;
            }
            endDate = endDateTVPicker.getValue().toString();
            if (startDate.compareTo(endDate) > -1) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid input");
                    alert.setContentText("Start date should be before end date.");
                    alert.showAndWait();
                });
                return;
            }
        } catch (NullPointerException e) {
            return;
        }

        ExchangeService.exchangeApi().getVolumeOfTransactions("Bearer " + Authentication.getInstance().getToken(),
                        startDate, endDate
                )
                .enqueue(new Callback<Map<String, Float>>() {
                    @Override
                    public void onResponse(Call<Map<String, Float>> call,
                                           Response<Map<String, Float>> response) {
                        Platform.runLater(() -> {
                            if (response.isSuccessful()) {
                                transactionVolumeLabel1.setText("USD " + response.body().get("usd_volume").toString());
                                transactionVolumeLabel2.setText("LBP " + response.body().get("lbp_volume").toString());
                            }
                            else {
                                Alerts.showResponse(response);
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<Map<String, Float>> call,
                                          Throwable throwable) {
                        Alerts.connectionFailure();
                    }
                });
    }

}
