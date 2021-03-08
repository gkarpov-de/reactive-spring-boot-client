package gk.restdemo.stockclient;

import reactor.core.publisher.Flux;

public interface StockClient {
    Flux<StockPrice> priceFor(String symbol);
}
