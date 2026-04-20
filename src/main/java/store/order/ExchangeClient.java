package store.order;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "exchange", url = "${clients.exchange.url}")
public interface ExchangeClient {

    @GetMapping("/exchanges/{from}/{to}")
    ExchangeOut getRate(@PathVariable String from, @PathVariable String to);
}