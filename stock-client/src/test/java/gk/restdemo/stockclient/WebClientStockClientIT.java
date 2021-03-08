package gk.restdemo.stockclient;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class WebClientStockClientIT {
    private final WebClient webClient = WebClient.builder().build();

    @Test
    @DisplayName("should  retrieve stock prices from service")
    void shouldRetrieveStockPricesFromService() {
        final StockClient webClientStockClient = new WebClientStockClient(webClient);
        final Flux<StockPrice> prices = webClientStockClient.priceFor("SYMBOL");

        assertNotNull(prices);
        final Flux<StockPrice> fivePrices = prices.take(5);
        assertEquals(5, fivePrices.count().block());
        assertEquals("SYMBOL", fivePrices.blockFirst().getSymbol());
    }
}