package gk.restdemo.stockclient;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;

import java.io.IOException;
import java.time.Duration;

@Log4j2
public class WebClientStockClient implements StockClient {
    private final WebClient webClient;

    public WebClientStockClient(final WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Flux<StockPrice> priceFor(final String symbol) {
        return webClient.get()
                .uri("http://localhost:8080/stocks/{symbol}", symbol)
                .retrieve()
                .bodyToFlux(StockPrice.class)
                .retryWhen(Retry.backoff(5, Duration.ofSeconds(1)).maxBackoff(Duration.ofSeconds(20)))
                .doOnError(IOException.class, e -> log.error(e.getMessage()));
    }
}
