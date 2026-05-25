package store.order;

import java.util.UUID;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import store.product.ProductController;
import store.product.ProductOut;

@Service
public class ProductCacheService {

    private final ProductController productController;

    public ProductCacheService(ProductController productController) {
        this.productController = productController;
    }

    @Cacheable(value = "product-prices", key = "#productId")
    public ProductOut getProduct(String productId) {
        return productController.findById(UUID.fromString(productId)).getBody();
    }
}
