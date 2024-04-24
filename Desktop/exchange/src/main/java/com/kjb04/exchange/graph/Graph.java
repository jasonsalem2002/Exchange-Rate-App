package com.kjb04.exchange.graph;

import com.kjb04.exchange.Authentication;
import com.kjb04.exchange.DateParser;
import com.kjb04.exchange.api.ExchangeService;
import com.kjb04.exchange.api.model.Message;
import javafx.application.Platform;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.*;

public class Graph {
    public LineChart lineChart;
    public DatePicker startDatePicker;
    public DatePicker endDatePicker;
    public ComboBox<String> granularityComboBox;

    public void initialize() {



        granularityComboBox.getItems().addAll("daily","weekly","monthly","yearly");
    }

    private List<Map<String,Float>> fetchAvgRates() {
        String startDate = startDatePicker.getValue().toString();
        String endDate = endDatePicker.getValue().toString();
        String granularity = granularityComboBox.getValue();
        final List<Map<String, Float>>[] avgRates = new List[]{new ArrayList<>()};


        ExchangeService.exchangeApi().getAvgRates("Bearer " + Authentication.getInstance().getToken(),
                        startDate,endDate,granularity
                )
                .enqueue(new Callback<List<Map<String,Float>>>() {
                    @Override
                    public void onResponse(Call<List<Map<String,Float>>> call,
                                           Response<List<Map<String,Float>>> response) {
                        startDatePicker.setValue(null);
                        endDatePicker.setValue(null);
                        avgRates[0] = response.body();
                    }
                    @Override
                    public void onFailure(Call<List<Map<String,Float>>> call,
                                          Throwable throwable) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Graph failed");
                        alert.setContentText("Failed to fetch average rates.");
                        alert.showAndWait();
                    }
                });
        return avgRates[0];
    }

    public void graph() {
        XYChart.Series<Date, Number> series = new XYChart.Series<>();
        series.setName("Sample Data");

        // Sample data array
        List<Map<String,Float>> data = fetchAvgRates();

        for (String x : data.get(0).keySet()) {
            series.getData().add(new XYChart.Data<>(DateParser.convertStringToDate(x), data.get(0).get(x)));
        }

        lineChart.getData().add(series);
    }


}
