package gk.restdemo.stockclient;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest
class RSocketStockClientIT {
    @Autowired
    private RSocketRequester.Builder builder;

    private RSocketRequester createRSocketRequester() {
//        return RSocketRequester.builder().tcp("localhost", 7000);
        return builder.tcp("localhost", 7000);
    }

    @Test
    @DisplayName("should  retrieve stock prices from service")
    void shouldRetrieveStockPricesFromService() {
        // given
        final RSocketStockClient rSocketStockClient = new RSocketStockClient(createRSocketRequester());
        final Flux<StockPrice> prices = rSocketStockClient.priceFor("SYMBOL");

        StepVerifier.create(prices.take(5))
                .expectNextMatches(stockPrice -> stockPrice.getSymbol().equals("SYMBOL"))
                .expectNextMatches(stockPrice -> stockPrice.getSymbol().equals("SYMBOL"))
                .expectNextMatches(stockPrice -> stockPrice.getSymbol().equals("SYMBOL"))
                .expectNextMatches(stockPrice -> stockPrice.getSymbol().equals("SYMBOL"))
                .expectNextMatches(stockPrice -> stockPrice.getSymbol().equals("SYMBOL"))
                .verifyComplete();
    }

}