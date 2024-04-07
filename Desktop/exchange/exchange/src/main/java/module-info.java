module com.kjb04.exchange {
    requires javafx.controls;
    requires javafx.fxml;
    requires retrofit2;
    requires java.sql;
    requires gson;
    requires retrofit2.converter.gson;
    opens com.kjb04.exchange to javafx.fxml;
    opens com.kjb04.exchange.api.model to gson;
    exports com.kjb04.exchange;
    exports com.kjb04.exchange.rates;
    opens com.kjb04.exchange.rates to javafx.fxml;
}
