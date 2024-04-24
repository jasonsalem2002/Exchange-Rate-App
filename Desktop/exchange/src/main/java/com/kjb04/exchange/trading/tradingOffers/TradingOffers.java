package com.kjb04.exchange.trading.tradingOffers;

import com.kjb04.exchange.Authentication;
import com.kjb04.exchange.api.ExchangeService;
import com.kjb04.exchange.api.model.Message;
import com.kjb04.exchange.api.model.Offer;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.net.URL;
import java.nio.channels.SelectableChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class TradingOffers implements Initializable {
    public TableColumn buttonColumn;
    public TableColumn offerID;
    public TableColumn usdToLbp;
    public TableColumn amountToTrade;
    public TableColumn amountRequested;
    public TableColumn<Offer,Float> rate;
    public TableColumn addedDate;
    public TableView tableView;
    public HBox allOffersDirectionBox;
    public ToggleGroup transactionType;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        transactionType.selectToggle(transactionType.getToggles().getFirst());
        selectAllOffers();
    }

    public void refreshOffers() {
        selectAllOffers();
    }

    private void acceptOffer(Offer o) {
        ExchangeService.exchangeApi().acceptOffer("Bearer " + Authentication.getInstance().getToken(), o.getId())
                .enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object>
                    response) {
                selectAllOffers();
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
    
    private List<Offer> filterOffers(List<Offer> offers, String type) {
        String username = Authentication.getInstance().getUsername();
        return switch (type) {
            case "USD_TO_LBP" -> offers.stream()
                    .filter(offer -> (!offer.getUsername().equals(username) && offer.getUsdToLbp().equals(true)))
                    .toList();
            case "LBP_TO_USD" -> offers.stream()
                    .filter(offer -> (!offer.getUsername().equals(username) && !offer.getUsdToLbp()))
                    .toList();
            case "PENDING" -> offers.stream()
                    .filter(offer -> (offer.getUsername().equals(username)))
                    .toList();
            default -> null;
        };

    }

    public void selectAllOffers() {
        tableView.getItems().clear();
        buttonColumn.setVisible(true);
        allOffersDirectionBox.setVisible(true);

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
                        RadioButton radioButton = (RadioButton) transactionType.getSelectedToggle();
                        List<Offer> offers = new ArrayList<Offer>();
                        if (radioButton.getText().equals("Buy USD")) {
                            offers = filterOffers(response.body(),"USD_TO_LBP");
                        }
                        else if (radioButton.getText().equals("Sell USD ")) {
                            offers = filterOffers(response.body(),"LBP_TO_USD");
                        }
                        tableView.getItems().setAll(offers);

                        rate.setCellValueFactory(cellData -> {
                            Offer offer = cellData.getValue(); // Get the Offer instance for the current row
                            Float amountOffered = offer.getAmountToTrade();
                            Float amountRequested = offer.getAmountRequested();
                            if (offer.getUsdToLbp()) {
                                return new ReadOnlyObjectWrapper<>(amountRequested / amountOffered);
                            }
                            else {
                                return new ReadOnlyObjectWrapper<>(amountRequested * amountOffered);
                            }
                        });

                        buttonColumn.setCellFactory(col -> new TableCell<Offer, Void>() {
                            private final Button btn = new Button("Accept");
                            {
                                btn.setOnAction(event -> {
                                    Offer offer = getTableView().getItems().get(getIndex());
                                    acceptOffer(offer);
                                });
                            }
                            @Override
                            protected void updateItem(Void item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                }
                                else {
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

    public void selectPending() {
        tableView.getItems().clear();
        buttonColumn.setVisible(false);
        allOffersDirectionBox.setVisible(false);


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
                        List<Offer> offers = filterOffers(response.body(),"PENDING");
                        tableView.getItems().setAll(offers);

                        rate.setCellValueFactory(cellData -> {
                            Offer offer = cellData.getValue(); // Get the Offer instance for the current row
                            Float amountOffered = offer.getAmountToTrade();
                            Float amountRequested = offer.getAmountRequested();
                            if (offer.getUsdToLbp()) {
                                return new ReadOnlyObjectWrapper<>(amountRequested / amountOffered);
                            }
                            else {
                                return new ReadOnlyObjectWrapper<>(amountOffered / amountRequested);
                            }
                        });
                    }
                    @Override
                    public void onFailure(Call<List<Offer>> call,
                                          Throwable throwable) {
                    }
                });

    }

    public void selectAccepted() {
        tableView.getItems().clear();
        buttonColumn.setVisible(false);
        allOffersDirectionBox.setVisible(false);
        addedDate.setVisible(false);

        offerID.setCellValueFactory(new
                PropertyValueFactory<Offer, Integer>("id"));
        usdToLbp.setCellValueFactory(new
                PropertyValueFactory<Offer, String>("usdToLbp"));
        amountToTrade.setCellValueFactory(new
                PropertyValueFactory<Offer, Float>("amountToTrade"));
        amountRequested.setCellValueFactory(new
                PropertyValueFactory<Offer, Float>("amountRequested"));

        ExchangeService.exchangeApi().getAcceptedOffers("Bearer " +
                        Authentication.getInstance().getToken())
                .enqueue(new Callback<List<Offer>>() {
                    @Override
                    public void onResponse(Call<List<Offer>> call,
                                           Response<List<Offer>> response) {
                        tableView.getItems().setAll(response.body());

                        rate.setCellValueFactory(cellData -> {
                            Offer offer = cellData.getValue(); // Get the Offer instance for the current row
                            Float amountOffered = offer.getAmountToTrade();
                            Float amountRequested = offer.getAmountRequested();
                            if (offer.getUsdToLbp()) {
                                return new ReadOnlyObjectWrapper<>(amountRequested / amountOffered);
                            }
                            else {
                                return new ReadOnlyObjectWrapper<>(amountRequested * amountOffered);
                            }
                        });
                    }
                    @Override
                    public void onFailure(Call<List<Offer>> call,
                                          Throwable throwable) {
                    }
                });

    }


}