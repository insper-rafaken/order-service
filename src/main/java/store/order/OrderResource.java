package store.order;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderResource implements OrderController {

    private final OrderService service;

    public OrderResource(OrderService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<OrderOut> create(OrderIn in) {
        Order order = service.create(OrderParser.to(in));
        return ResponseEntity.status(201).body(OrderParser.to(order));
    }

    @Override
    public ResponseEntity<List<OrderOut>> findAll() {
        return ResponseEntity.ok(OrderParser.to(service.findAll()));
    }

    @Override
    public ResponseEntity<OrderOut> findById(String id, String currency, String idAccount) {
        Order order = service.findById(id, currency, idAccount);
        if (order == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(OrderParser.to(order));
    }
}
