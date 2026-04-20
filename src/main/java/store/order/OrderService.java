package store.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderRepository repository;

    @Autowired
    private ProductClient productClient;

    @Autowired
    private ExchangeClient exchangeClient;

    public Order create(Order order) {
        order.total(10.0);
        OrderModel saved = repository.save(OrderParser.toModel(order));
        return OrderParser.to(saved);
    }

    public List<Order> findAll() {
        return repository.findAll().stream()
            .map(OrderParser::to)
            .toList();
    }

    public Order findById(String id, String currency) {
        Order order = OrderParser.to(
            repository.findById(id).orElse(null)
        );

        if (order == null) return null;

        if (currency != null){
            order.total(order.total() * 5.85).currency(currency.toUpperCase());
        }
        

        return order;
    }
}