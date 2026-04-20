package store.order;

import java.time.LocalDateTime;
import java.util.List;

public class OrderParser {

    public static Order to(OrderIn in) {
        if (in == null) return null;
        return Order.builder()
            .accountId(in.accountId())
            .currency("BRL")
            .createdAt(LocalDateTime.now())
            .items(in.items() == null ? List.of() :
                in.items().stream().map(OrderParser::to).toList())
            .build();
    }

    public static OrderItem to(OrderItemIn in) {
        if (in == null) return null;
        return OrderItem.builder()
            .productId(in.idProduct())
            .quantity(in.quantity())
            .price(0.0)
            .build();
    }

    public static OrderOut to(Order o) {
        if (o == null) return null;
        return OrderOut.builder()
            .id(o.id())
            .createdAt(o.createdAt())
            .currency(o.currency())
            .total(o.total())
            .items(o.items() == null ? List.of() :
                o.items().stream().map(OrderParser::to).toList())
            .build();
    }

    public static OrderItemOut to(OrderItem item) {
        if (item == null) return null;
        return OrderItemOut.builder()
            .idProduct(item.productId())
            .quantity(item.quantity())
            .price(item.price())
            .build();
    }

    public static List<OrderOut> to(List<Order> orders) {
        return orders.stream().map(OrderParser::to).toList();
    }

}