package store.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderRepository repository;

    public Order create(Order order) {
        OrderModel saved = repository.save(OrderParser.toModel(order));
        return OrderParser.to(saved);
    }

    public List<Order> findAll() {
        return repository.findAll().stream()
            .map(OrderParser::to)
            .toList();
    }

    public Order findById(String id) {
        return OrderParser.to(
            repository.findById(id).orElse(null)
        );
    }
}