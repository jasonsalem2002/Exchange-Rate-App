package com.kjb04.exchange.trading.tradingOffers;

import com.kjb04.exchange.Authentication;
import com.kjb04.exchange.api.ExchangeService;
import com.kjb04.exchange.api.model.Offer;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class TradingOffers implements Initializable {
    public TableColumn buttonColumn;
    public TableColumn offerID;
    public TableColumn usdToLbp;
    public TableColumn amountToTrade;
    public TableColumn amountRequested;
    public TableColumn rate;
    public TableColumn addedDate;
    public TableView tableView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        offerID.setCellValueFactory(new
                PropertyValueFactory<Offer, Integer>("id"));
        usdToLbp.setCellValueFactory(new
                PropertyValueFactory<Offer, String>("usdToLbp"));
        amountToTrade.setCellValueFactory(new
                PropertyValueFactory<Offer, Float>("amountToTrade"));
        amountRequested.setCellValueFactory(new
                PropertyValueFactory<Offer, Float>("amountRequested"));
        addedDate.setCellValueFactory(new
                PropertyValueFactory<Offer, String>("addedDate"));

        ExchangeService.exchangeApi().getOffers("Bearer " +
                        Authentication.getInstance().getToken())
            .enqueue(new Callback<List<Offer>>() {
                @Override
                public void onResponse(Call<List<Offer>> call,
                                       Response<List<Offer>> response) {
                    tableView.getItems().setAll(response.body());

                    rate.setCellValueFactory(cellData -> {
                        List<Float> rates = new ArrayList<Float>();
                        for (Offer o : response.body()) {
                            Float amountOffered = o.getAmountToTrade();
                            Float amountRequested = o.getAmountRequested();
                            rates.add(amountOffered * amountRequested);
                        }
                        return new ReadOnlyObjectWrapper<>(rates);
                    });

//                    buttonColumn.setCellFactory(cellData -> {
//                        List<Button> rates = new ArrayList<Button>();
//                        for (Offer o : response.body()) {
//                            Button btn = new Button("Accept");;
//                            btn.setOnAction(event -> {
//                                acceptOffer(o);
//                            });
//                            rates.add(btn);
//                        }
//                        return new ReadOnlyObjectWrapper<>(rates);
//                    });
                    buttonColumn.setCellFactory(col -> new TableCell<Offer, Offer>() {
                        private final Button btn = new Button("Accept");

                        @Override
                        protected void updateItem(Offer offer, boolean empty) {
                            super.updateItem(offer, empty);
                            if (empty || offer == null) {
                                setGraphic(null);
                            } else {
                                btn.setOnAction(event -> acceptOffer(offer));
                                setGraphic(btn);
                            }
                        }
                    });
                }
                @Override
                public void onFailure(Call<List<Offer>> call,
                                      Throwable throwable) {
                }
            });

    }

    public void refreshOffers() {
        ExchangeService.exchangeApi().getOffers("Bearer " +
                        Authentication.getInstance().getToken())
                .enqueue(new Callback<List<Offer>>() {
                    @Override
                    public void onResponse(Call<List<Offer>> call,
                                           Response<List<Offer>> response) {
                        tableView.getItems().setAll(response.body());

                        rate.setCellValueFactory(cellData -> {
                            List<Float> rates = new ArrayList<Float>();
                            for (Offer o : response.body()) {
                                Float amountOffered = o.getAmountToTrade();
                                Float amountRequested = o.getAmountRequested();
                                rates.add(amountOffered * amountRequested);
                            }
                            return new ReadOnlyObjectWrapper<>(rates);
                        });

//                    buttonColumn.setCellFactory(cellData -> {
//                        List<Button> rates = new ArrayList<Button>();
//                        for (Offer o : response.body()) {
//                            Button btn = new Button("Accept");;
//                            btn.setOnAction(event -> {
//                                acceptOffer(o);
//                            });
//                            rates.add(btn);
//                        }
//                        return new ReadOnlyObjectWrapper<>(rates);
//                    });
                        buttonColumn.setCellFactory(col -> new TableCell<Offer, Offer>() {
                            private final Button btn = new Button("Accept");

                            @Override
                            protected void updateItem(Offer offer, boolean empty) {
                                super.updateItem(offer, empty);
                                if (empty || offer == null) {
                                    setGraphic(null);
                                } else {
                                    btn.setOnAction(event -> acceptOffer(offer));
                                    setGraphic(btn);
                                }
                            }
                        });
                    }
                    @Override
                    public void onFailure(Call<List<Offer>> call,
                                          Throwable throwable) {
                    }
                });
    }

    private void acceptOffer(Offer o) {

        String userToken = Authentication.getInstance().getToken();
        String authHeader = userToken != null ? "Bearer " + userToken : null;
        ExchangeService.exchangeApi().acceptOffer(
                authHeader, o.getUserId()).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object>
                    response) {

            }

            @Override
            public void onFailure(Call<Object> call, Throwable throwable) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Transaction Failed");
                    alert.setContentText("Failed to add transaction.");
                    alert.showAndWait();
                });
            }
        });
    }

//    public void initButtonColumn() {
//        //buttonColumn.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));
//
//        Callback<TableColumn<Offer, String>> cellFactory
//                = //
//                new Callback<TableColumn<Offer, String>>() {
//                    @Override
//                    public void onResponse(Call<TableColumn<Offer, String>> call, Response<TableColumn<Offer, String>> response) {
//                            final TableCell<Offer, String> cell = new TableCell<Offer, String>() {
//
//                                final Button btn = new Button("Just Do It");
//
//                                @Override
//                                public void updateItem(String item, boolean empty) {
//                                    super.updateItem(item, empty);
//                                    if (empty) {
//                                        setGraphic(null);
//                                        setText(null);
//                                    } else {
//                                        btn.setOnAction(event -> {
//                                            Offer offer = getTableView().getItems().get(getIndex());
//                                        });
//                                        setGraphic(btn);
//                                        setText(null);
//                                    }
//                                }
//                            };
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<TableColumn<Offer, String>> call, Throwable throwable) {
//
//                    }
//
//
//                };
//        buttonColumn.setCellValueFactory((javafx.util.Callback<TableColumn.CellDataFeatures, ObservableValue>) cellFactory);
//
//    }
}