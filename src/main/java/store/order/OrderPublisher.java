package store.order;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OrderPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${order.rabbitmq.exchange}")
    private String exchange;

    @Value("${order.rabbitmq.routing-key}")
    private String routingKey;

    public OrderPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(OrderMessage message) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }
}
