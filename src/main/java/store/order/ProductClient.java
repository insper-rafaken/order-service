package store.order;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import store.product.ProductOut;

@FeignClient(name = "product-client", url = "${product.url}", configuration = ProductAuthConfig.class)
public interface ProductClient {

    @GetMapping("/products/{id}")
    ResponseEntity<ProductOut> findById(@PathVariable UUID id);

}
