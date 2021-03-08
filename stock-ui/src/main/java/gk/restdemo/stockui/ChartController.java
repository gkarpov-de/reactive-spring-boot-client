package gk.restdemo.stockui;

import gk.restdemo.stockclient.StockClient;
import gk.restdemo.stockclient.StockPrice;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Consumer;

import static java.lang.String.valueOf;
import static javafx.collections.FXCollections.observableArrayList;

@Component
public class ChartController {
    private final StockClient webClientStockClient;
    @FXML
    public LineChart<String, Double> chart;


    public ChartController(final StockClient webClientStockClient) {
        this.webClientStockClient = webClientStockClient;
    }

    public void initialize() {
        addSubscriberFor(List.of("SYMBOL1", "SYMBOL2"));
    }

    private void addSubscriberFor(final List<String> symbols) {
        final ObservableList<Series<String, Double>> data = observableArrayList();
        for (final String symbol : symbols) {
            final PriceSubscriber priceSubscriber = new PriceSubscriber(symbol);

            data.add(priceSubscriber.getSeries());
            chart.setData(data);

            webClientStockClient.priceFor(symbol).subscribe(priceSubscriber);

        }
    }

    private static class PriceSubscriber implements Consumer<StockPrice> {
        private final ObservableList<Data<String, Double>> seriesData = observableArrayList();
        private final Series<String, Double> series;

        public PriceSubscriber(final String symbol) {
            series = new Series<>(symbol, seriesData);
        }

        @Override
        public void accept(final StockPrice stockPrice) {
            Platform.runLater(() ->
                    seriesData.add(new Data<>(valueOf(stockPrice.getTime().getSecond()),
                            stockPrice.getPrice())));
        }

        public Series<String, Double> getSeries() {
            return series;
        }
    }

}
