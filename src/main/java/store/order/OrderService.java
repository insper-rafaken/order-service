package store.order;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import store.exchange.ExchangeController;
import store.exchange.ExchangeOut;
import store.product.ProductController;
import store.product.ProductOut;

@Service
public class OrderService {

    private final OrderRepository repository;
    private final ProductController productController;
    private final ExchangeController exchangeController;

    public OrderService(OrderRepository repository, ProductController productController, ExchangeController exchangeController) {
        this.repository = repository;
        this.productController = productController;
        this.exchangeController = exchangeController;
    }

    @Transactional
    public Order create(Order order) {
        List<OrderItem> enrichedItems = order.items().stream().map(item -> {
            ProductOut product = productController.findById(UUID.fromString(item.productId())).getBody();
            BigDecimal price = product.price();
            return item.price(price);
        }).toList();

        BigDecimal total = enrichedItems.stream()
            .map(item -> item.price().multiply(BigDecimal.valueOf(item.quantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order enriched = order.items(enrichedItems).total(total);
        OrderModel saved = repository.save(OrderParser.toModel(enriched));
        return OrderParser.to(saved);
    }

    @Transactional(readOnly = true)
    public List<Order> findAll() {
        return repository.findAll().stream()
            .map(OrderParser::to)
            .toList();
    }

    @Transactional(readOnly = true)
    public Order findById(String id, String currency, String authorization) {
        Order order = OrderParser.to(repository.findById(id).orElse(null));
        if (order == null) return null;

        if (currency != null && !currency.equalsIgnoreCase("BRL") && authorization != null) {
            ExchangeOut exchange = exchangeController.getExchange("BRL", currency, authorization).getBody();
            BigDecimal rate = BigDecimal.valueOf(exchange.sell());

            List<OrderItem> convertedItems = order.items().stream()
                .map(item -> item.price(item.price().multiply(rate)))
                .toList();

            BigDecimal convertedTotal = convertedItems.stream()
                .map(item -> item.price().multiply(BigDecimal.valueOf(item.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            return order.items(convertedItems).total(convertedTotal).currency(currency);
        }

        return order;
    }
}
