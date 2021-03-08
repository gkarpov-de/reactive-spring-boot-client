package gk.restdemo.stockclient;

import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.rsocket.RSocketRequester;
import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;

import java.io.IOException;
import java.time.Duration;

@Log4j2
public class RSocketStockClient implements StockClient {
    RSocketRequester rSocketRequester;

    public RSocketStockClient(final RSocketRequester rSocketRequester) {
        this.rSocketRequester = rSocketRequester;
    }

    @Override
    public Flux<StockPrice> priceFor(final String symbol) {
        return rSocketRequester.route("stockPrice")
                .data(symbol)
                .retrieveFlux(StockPrice.class)
                .retryWhen(Retry.backoff(5, Duration.ofSeconds(1)).maxBackoff(Duration.ofSeconds(20)))
                .doOnError(IOException.class, e -> log.error(e.getMessage()));
    }
}
