module com.kjb04.exchange {
    requires javafx.controls;
    requires javafx.fxml;
    requires retrofit2;
    requires java.sql;
    requires gson;
    requires retrofit2.converter.gson;
    exports com.kjb04.exchange;
    exports com.kjb04.exchange.rates;
    exports com.kjb04.exchange.trading;
    opens com.kjb04.exchange.rates to javafx.fxml;
    opens com.kjb04.exchange.trading to javafx.fxml;
    opens com.kjb04.exchange.trading.tradingOffers to javafx.fxml;
    opens com.kjb04.exchange.trading.tradingCreate to javafx.fxml;
    opens com.kjb04.exchange to javafx.fxml;
    opens com.kjb04.exchange.api.model to javafx.base, gson;
    requires java.prefs;
    opens com.kjb04.exchange.login to javafx.fxml;
    opens com.kjb04.exchange.register to javafx.fxml;
    opens com.kjb04.exchange.transactions to javafx.fxml;
}