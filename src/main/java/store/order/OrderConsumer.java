package store.order;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderConsumer {

    private final OrderService orderService;

    public OrderConsumer(OrderService orderService) {
        this.orderService = orderService;
    }

    @RabbitListener(queues = "${order.rabbitmq.queue}")
    public void consume(OrderMessage message) {
        orderService.processOrder(message.orderId());
    }
}
