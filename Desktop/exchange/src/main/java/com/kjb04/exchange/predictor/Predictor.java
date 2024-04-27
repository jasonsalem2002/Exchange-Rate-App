package com.kjb04.exchange.predictor;

import com.kjb04.exchange.Alerts;
import com.kjb04.exchange.Authentication;
import com.kjb04.exchange.api.ExchangeService;
import com.kjb04.exchange.api.model.FutureRate;
import javafx.application.Platform;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Predictor {
    public LineChart lineChart;
    public DatePicker datePicker;
    public Label predictionLabel;
    public NumberAxis yAxis;

    public void initialize() {
        XYChart.Series<String, Number> buyUSDSeries = new XYChart.Series<>();
        XYChart.Series<String, Number> sellUSDSeries = new XYChart.Series<>();

        String endDate = LocalDate.now().toString();
        String startDate = LocalDate.parse(endDate).minusDays(30).toString();

        ExchangeService.exchangeApi().getAvgRates("Bearer " + Authentication.getInstance().getToken(),
                        startDate,endDate,"daily"
                )
                .enqueue(new Callback<Map<String, Map<String, Float>>>() {
                    @Override
                    public void onResponse(Call<Map<String,Map<String, Float>>> call,
                                           Response<Map<String,Map<String, Float>>> response) {
                        Platform.runLater(()->{
                            if (response.isSuccessful()) {
                                lineChart.getData().clear();
                                lineChart.setTitle("daily exchange rate: last 30 days and next 30 days");
                                Map<String, Map<String, Float>> data = response.body();

                                if (data == null) {
                                    Platform.runLater(() -> {
                                        Alert alert = new Alert(Alert.AlertType.ERROR);
                                        alert.setTitle("Data Unavailable");
                                        alert.setContentText("Daily rates for the last 30 days are not available at this time.");
                                        alert.showAndWait();
                                    });
                                } else {
                                    if (data != null && data.get("lbp_to_usd") != null && !data.get("lbp_to_usd").isEmpty()) {
                                        for (String x : data.get("lbp_to_usd").keySet()) {
                                            buyUSDSeries.getData().add(new XYChart.Data<>(x, Math.round(data.get("lbp_to_usd").get(x) * 100) / 100.0f));
                                        }
                                        buyUSDSeries.setName("Buy USD");
                                        lineChart.getData().add(buyUSDSeries);
                                    }
                                    if (data != null && data.get("usd_to_lbp") != null && !data.get("usd_to_lbp").isEmpty()) {
                                        for (String x : data.get("usd_to_lbp").keySet()) {
                                            sellUSDSeries.getData().add(new XYChart.Data<>(x, Math.round(data.get("usd_to_lbp").get(x) * 100) / 100.0f));
                                        }
                                        sellUSDSeries.setName("Sell USD");
                                        lineChart.getData().add(sellUSDSeries);
                                    }
                                }
                                Float min = Math.min(Collections.min(data.get("lbp_to_usd").values()), Collections.min(data.get("usd_to_lbp").values()));
                                Float max = Math.max(Collections.max(data.get("lbp_to_usd").values()), Collections.max(data.get("usd_to_lbp").values()));
                                yAxis.setTickUnit((max + min + (max - min) / 2) / 2000);
                                yAxis.setLowerBound(min - (max - min) / 4);
                                yAxis.setUpperBound(max + (max - min) / 4);
                                displayPredGraph();
                            }
                            else {
                                Alerts.showResponse(response);
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

    private void displayPredGraph() {
        XYChart.Series<String, Number> predSeries = new XYChart.Series<>();

        ExchangeService.exchangeApi().getNext30DaysRates(
                "Bearer " + Authentication.getInstance().getToken())
                .enqueue(new Callback<List<FutureRate>>() {
                    @Override
                    public void onResponse(Call<List<FutureRate>> call,
                                           Response<List<FutureRate>> response) {
                        if (response.isSuccessful()) {
                            Platform.runLater(() -> {
                                List<FutureRate> data = response.body();
                                if (data == null) {
                                    Platform.runLater(() -> {
                                        Alert alert = new Alert(Alert.AlertType.ERROR);
                                        alert.setTitle("Data Unavailable");
                                        alert.setContentText("This data is not available at this time.");
                                        alert.showAndWait();
                                    });
                                } else {
                                    for (FutureRate x : data) {
                                        predSeries.getData().add(new XYChart.Data<>(x.getDate(), Math.round(x.getAverageExchangeRate() * 100) / 100.0f));
                                    }
                                    lineChart.getData().add(predSeries);
                                }
                            });
                        }
                        else {
                            Alerts.showResponse(response);
                        }
                    }
                    @Override
                    public void onFailure(Call<List<FutureRate>> call,
                                          Throwable throwable) {
                        Alerts.connectionFailure();
                    }
                });
    }

    public void predictRate() {
        String date = datePicker.getValue().toString();
        if (date.compareTo(LocalDate.now().toString()) < 1) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid input");
                alert.setContentText("Please input a future date.");
                alert.showAndWait();
            });
            return;
        }
        if (date.compareTo(LocalDate.ofYearDay(2030,1).toString()) > 0) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid input");
                alert.setContentText("Please input a date before 2030.");
                alert.showAndWait();
            });
            return;
        }

        ExchangeService.exchangeApi().getPredRate(
                "Bearer " + Authentication.getInstance().getToken(), date)
                .enqueue(new Callback<FutureRate>() {
                    @Override
                    public void onResponse(Call<FutureRate> call,
                                           Response<FutureRate> response) {
                        Platform.runLater(()->{
                            if (response.isSuccessful()) {
                                predictionLabel.setText("Predicted rate for " + response.body().getDate() +
                                        ": " + response.body().getAverageExchangeRate());
                            }
                            else {
                                Alerts.showResponse(response);
                            }
                        });
                    }
                    @Override
                    public void onFailure(Call<FutureRate> call,
                                          Throwable throwable) {
                        Alerts.connectionFailure();
                    }
                });
    }

}
