package store.order;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository repository;

    public OrderService(OrderRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Order create(Order order) {
        OrderModel saved = repository.save(OrderParser.toModel(order));
        return OrderParser.to(saved);
    }

    @Transactional(readOnly = true)
    public List<Order> findAll() {
        return repository.findAll().stream()
            .map(OrderParser::to)
            .toList();
    }

    @Transactional(readOnly = true)
    public Order findById(String id) {
        return OrderParser.to(
            repository.findById(id).orElse(null)
        );
    }
}
