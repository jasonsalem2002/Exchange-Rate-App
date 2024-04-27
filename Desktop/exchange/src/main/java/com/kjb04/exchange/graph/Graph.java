//package com.kjb04.exchange.graph;
//
//import com.kjb04.exchange.Authentication;
//import com.kjb04.exchange.DateParser;
//import com.kjb04.exchange.api.ExchangeService;
//import com.kjb04.exchange.api.model.Message;
//import javafx.application.Platform;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.scene.chart.LineChart;
//import javafx.scene.chart.XYChart;
//import javafx.scene.control.Alert;
//import javafx.scene.control.ComboBox;
//import javafx.scene.control.DatePicker;
//import javafx.scene.control.Label;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.*;
//
//public class Graph {
//    public LineChart<String, Number> lineChart;
//    public DatePicker startDatePicker;
//    public DatePicker endDatePicker;
//    public ComboBox<String> granularityComboBox;
//    public Label granNoteLabel;
//
//    public void initialize() {
//        granularityComboBox.getItems().addAll("daily","weekly","monthly","yearly");
//        displayInitGraph();
//    }
//
//    private void displayInitGraph() {
//
//        XYChart.Series<String, Number> buyUSDSeries = new XYChart.Series<>();
//        XYChart.Series<String, Number> sellUSDSeries = new XYChart.Series<>();
//        buyUSDSeries.setName("Buy USD");
//        sellUSDSeries.setName("Sell USD");
//
//        String endDate = parseDateNow(LocalDate.now().toString());
//        String startDate = LocalDate.parse(endDate).minusDays(30).toString();
//
//        ExchangeService.exchangeApi().getAvgRates("Bearer " + Authentication.getInstance().getToken(),
//                        startDate,endDate,"daily"
//                )
//                .enqueue(new Callback<Map<String,Map<String, Float>>>() {
//                    @Override
//                    public void onResponse(Call<Map<String,Map<String, Float>>> call,
//                                           Response<Map<String,Map<String, Float>>> response) {
//                        Platform.runLater(()->{
//                            lineChart.getData().clear();
//                            lineChart.setTitle("Daily exchange rate, last 30 days");
//                            startDatePicker.setValue(null);
//                            endDatePicker.setValue(null);
//                            Map<String, Map<String, Float>> data = response.body();
//
//                            for (String x : data.get("lbp_to_usd").keySet()) {
//                                buyUSDSeries.getData().add(new XYChart.Data<>(x, data.get("lbp_to_usd").get(x)));
//                            }
//                            lineChart.getData().add(buyUSDSeries);
//                            for (String x : data.get("usd_to_lbp").keySet()) {
//                                sellUSDSeries.getData().add(new XYChart.Data<>(x, data.get("usd_to_lbp").get(x)));
//                            }
//                            lineChart.getData().add(sellUSDSeries);
//                        });
//                    }
//                    @Override
//                    public void onFailure(Call<Map<String,Map<String, Float>>> call,
//                                          Throwable throwable) {
//                        Platform.runLater(()-> {
//                            Alert alert = new Alert(Alert.AlertType.ERROR);
//                            alert.setTitle("Graph failed");
//                            alert.setContentText("Failed to fetch average rates.");
//                            alert.showAndWait();
//                        });
//                    }
//                });
//    }
//
//    public void graph() {
//        XYChart.Series<String, Number> buyUSDSeries = new XYChart.Series<>();
//        XYChart.Series<String, Number> sellUSDSeries = new XYChart.Series<>();
//        buyUSDSeries.setName("Buy USD");
//        sellUSDSeries.setName("Sell USD");
//
//        String startDate = startDatePicker.getValue().toString();
//        String endDate = endDatePicker.getValue().toString();
//        String granularity = granularityComboBox.getValue();
//
//
//        ExchangeService.exchangeApi().getAvgRates("Bearer " + Authentication.getInstance().getToken(),
//                        startDate,endDate,granularity
//                )
//                .enqueue(new Callback<Map<String,Map<String, Float>>>() {
//                    @Override
//                    public void onResponse(Call<Map<String,Map<String, Float>>> call,
//                                           Response<Map<String,Map<String, Float>>> response) {
//                        Platform.runLater(()->{
//                            lineChart.getData().clear();
//                            startDatePicker.setValue(null);
//                            endDatePicker.setValue(null);
//                            Map<String, Map<String, Float>> data = response.body();
//
//                            for (String x : data.get("lbp_to_usd").keySet()) {
//                                buyUSDSeries.getData().add(new XYChart.Data<>(x, data.get("lbp_to_usd").get(x)));
//                            }
//                            lineChart.getData().add(buyUSDSeries);
//                            for (String x : data.get("usd_to_lbp").keySet()) {
//                                sellUSDSeries.getData().add(new XYChart.Data<>(x, data.get("usd_to_lbp").get(x)));
//                            }
//                            lineChart.getData().add(sellUSDSeries);
//                        });
//                    }
//                    @Override
//                    public void onFailure(Call<Map<String,Map<String, Float>>> call,
//                                          Throwable throwable) {
//                        Platform.runLater(()-> {
//                            Alert alert = new Alert(Alert.AlertType.ERROR);
//                            alert.setTitle("Graph failed");
//                            alert.setContentText("Failed to fetch average rates.");
//                            alert.showAndWait();
//                        });
//                    }
//                });
//    }
//
//    public void displayGranNote() {
//        if (granularityComboBox.getValue().equals("daily")) {
//            granNoteLabel.setText("Daily granularity: only up to 60 days of data.");
//        }
//        else if (granularityComboBox.getValue().equals("weekly")) {
//            granNoteLabel.setText("Weekly granularity: only up to 52 weeks of data.");
//        }
//        else if (granularityComboBox.getValue().equals("monthly")) {
//            granNoteLabel.setText("Monthly granularity: only up to 12 months of data.");
//        }
//        else {
//            granNoteLabel.setText("");
//        }
//    }
//
//    public String parseDateNow(String date) {
//        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        LocalDate formattedDate = LocalDate.parse(date, inputFormatter);
//        return formattedDate.format(outputFormatter);
//    }
//
//
//}






package com.kjb04.exchange.graph;

import com.kjb04.exchange.Alerts;
import com.kjb04.exchange.Authentication;
import com.kjb04.exchange.DateParser;
import com.kjb04.exchange.api.ExchangeService;
import com.kjb04.exchange.api.model.Message;
import javafx.application.Platform;
import javafx.event.ActionEvent;
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

public class Graph {
    public LineChart<String, Number> lineChart;
    public DatePicker startDatePicker;
    public DatePicker endDatePicker;
    public ComboBox<String> granularityComboBox;
    public Label granNoteLabel;
    public NumberAxis yAxis;

    public void initialize() {
        granularityComboBox.getItems().addAll("daily","weekly","monthly","yearly");
        displayInitGraph();
    }

    private void displayInitGraph() {

        XYChart.Series<String, Number> buyUSDSeries = new XYChart.Series<>();
        XYChart.Series<String, Number> sellUSDSeries = new XYChart.Series<>();

        String endDate = LocalDate.now().toString();
        String startDate = LocalDate.parse(endDate).minusDays(30).toString();

        ExchangeService.exchangeApi().getAvgRates("Bearer " + Authentication.getInstance().getToken(),
                        startDate,endDate,"daily"
                )
                .enqueue(new Callback<Map<String,Map<String, Float>>>() {
                    @Override
                    public void onResponse(Call<Map<String,Map<String, Float>>> call,
                                           Response<Map<String,Map<String, Float>>> response) {
                        Platform.runLater(()->{
                            lineChart.getData().clear();
                            lineChart.setTitle("Daily exchange rate, last 30 days");
                            Map<String, Map<String, Float>> data = response.body();

                            if (data == null) {
                                Platform.runLater(() -> {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("Data Unavailable");
                                    alert.setContentText("This data is not available at this time.");
                                    alert.showAndWait();
                                });
                            }
                            else {
                                if (data.get("lbp_to_usd") != null && !data.get("lbp_to_usd").isEmpty()) {
                                    for (String x : data.get("lbp_to_usd").keySet()) {
                                        buyUSDSeries.getData().add(new XYChart.Data<>(x, Math.round(data.get("lbp_to_usd").get(x) * 100) / 100.0f));    // Solves an error where points don't show if there are Float values that are very close to each other
                                    }
                                    buyUSDSeries.setName("Buy USD");
                                    lineChart.getData().add(buyUSDSeries);
                                }
                                if (data.get("usd_to_lbp") != null && !data.get("usd_to_lbp").isEmpty()) {
                                    for (String x : data.get("usd_to_lbp").keySet()) {
                                        sellUSDSeries.getData().add(new XYChart.Data<>(x, Math.round(data.get("usd_to_lbp").get(x) * 100) / 100.0f));
                                    }
                                    sellUSDSeries.setName("Sell USD");
                                    lineChart.getData().add(sellUSDSeries);
                                }
                                Float min = Math.min(Collections.min(data.get("lbp_to_usd").values()),Collections.min(data.get("usd_to_lbp").values()));
                                Float max = Math.max(Collections.max(data.get("lbp_to_usd").values()),Collections.max(data.get("usd_to_lbp").values()));
                                yAxis.setTickUnit((max + min + (max-min)/2)/2000);
                                yAxis.setLowerBound(min - (max-min)/4);
                                yAxis.setUpperBound(max + (max-min)/4);
                            }
                        });
                    }
                    @Override
                    public void onFailure(Call<Map<String,Map<String, Float>>> call,
                                          Throwable throwable) {
                        Alerts.connectionFailure();
                    }
                });
    }

    public void graph() {
        XYChart.Series<String, Number> buyUSDSeries = new XYChart.Series<>();
        XYChart.Series<String, Number> sellUSDSeries = new XYChart.Series<>();
        buyUSDSeries.setName("Buy USD");
        sellUSDSeries.setName("Sell USD");

        String startDate = startDatePicker.getValue().toString();
        String endDate = endDatePicker.getValue().toString();
        String granularity = granularityComboBox.getValue();


        ExchangeService.exchangeApi().getAvgRates("Bearer " + Authentication.getInstance().getToken(),
                        startDate,endDate,granularity
                )
                .enqueue(new Callback<Map<String,Map<String, Float>>>() {
                    @Override
                    public void onResponse(Call<Map<String,Map<String, Float>>> call,
                                           Response<Map<String,Map<String, Float>>> response) {
                        Platform.runLater(()-> {
                            lineChart.getData().clear();
                            startDatePicker.setValue(null);
                            endDatePicker.setValue(null);
                            Map<String, Map<String, Float>> data = response.body();
                            lineChart.setTitle(granularity + " exchange rate, " + startDate + " to " + endDate);//TODO test
                            if (data == null) {
                                Platform.runLater(() -> {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("Data Unavailable");
                                    alert.setContentText("This data is not available at this time.");
                                    alert.showAndWait();
                                });
                            }
                            else {
                                if (data.get("lbp_to_usd") != null && !data.get("lbp_to_usd").isEmpty()) {
                                    for (String x : data.get("lbp_to_usd").keySet()) {
                                        buyUSDSeries.getData().add(new XYChart.Data<>(x, Math.round(data.get("lbp_to_usd").get(x) * 100) / 100.0f));
                                    }
                                    buyUSDSeries.setName("Buy USD");
                                    lineChart.getData().add(buyUSDSeries);
                                }
                                if (data.get("usd_to_lbp") != null && !data.get("usd_to_lbp").isEmpty()) {
                                    for (String x : data.get("usd_to_lbp").keySet()) {
                                        sellUSDSeries.getData().add(new XYChart.Data<>(x, Math.round(data.get("usd_to_lbp").get(x) * 100) / 100.0f));
                                    }
                                    sellUSDSeries.setName("Sell USD");
                                    lineChart.getData().add(sellUSDSeries);
                                }
                                Float min = Math.min(Collections.min(data.get("lbp_to_usd").values()),Collections.min(data.get("usd_to_lbp").values()));
                                Float max = Math.max(Collections.max(data.get("lbp_to_usd").values()),Collections.max(data.get("usd_to_lbp").values()));
                                yAxis.setTickUnit((max + min + (max-min)/2)/2000);
                                yAxis.setLowerBound(min - (max-min)/4);
                                yAxis.setUpperBound(max + (max-min)/4);
                            }
                        });
                    }
                    @Override
                    public void onFailure(Call<Map<String,Map<String, Float>>> call,
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

//    public String parseDateNow(String date) {
//        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        LocalDate formattedDate = LocalDate.parse(date, inputFormatter);
//        return formattedDate.format(outputFormatter);
//    }


}
