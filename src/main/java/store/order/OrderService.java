package store.order;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import store.exchange.ExchangeController;
import store.exchange.ExchangeOut;
import store.product.ProductController;
import store.product.ProductOut;

@Service
public class OrderService {

    private final OrderRepository repository;
    private final ProductController productController;
    private final ExchangeController exchangeController;
    private final OrderPublisher publisher;

    public OrderService(OrderRepository repository, ProductController productController,
            ExchangeController exchangeController, OrderPublisher publisher) {
        this.repository = repository;
        this.productController = productController;
        this.exchangeController = exchangeController;
        this.publisher = publisher;
    }

    @Transactional
    public Order create(Order order) {
        OrderModel saved = repository.save(OrderParser.toModel(order));
        String orderId = saved.getId();

        // publica na fila apenas após o commit para evitar race condition
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                publisher.publish(new OrderMessage(orderId));
            }
        });

        return OrderParser.to(saved);
    }

    @Transactional
    public void processOrder(String orderId) {
        OrderModel model = repository.findById(orderId).orElseThrow();

        model.getItems().forEach(item -> {
            ProductOut product = productController.findById(UUID.fromString(item.getProductId())).getBody();
            item.setPrice(product.price());
        });

        BigDecimal total = model.getItems().stream()
            .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.setTotal(total);
        model.setStatus("CONFIRMED");
        repository.save(model);
    }

    @Transactional(readOnly = true)
    public List<Order> findAll() {
        return repository.findAll().stream()
            .map(OrderParser::to)
            .toList();
    }

    @Transactional(readOnly = true)
    public Order findById(String id, String currency, String idAccount) {
        Order order = OrderParser.to(repository.findById(id).orElse(null));
        if (order == null) return null;

        if (currency != null && !currency.equalsIgnoreCase("BRL") && idAccount != null) {
            ExchangeOut exchange = exchangeController.getExchange("BRL", currency, idAccount).getBody();
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
