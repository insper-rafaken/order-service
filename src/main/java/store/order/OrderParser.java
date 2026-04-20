package store.order;

import java.time.LocalDateTime;
import java.util.List;

public class OrderParser {

    public static Order to(OrderIn in) {
        if (in == null) return null;

        var items = in.items() == null ? List.<OrderItem>of() :
            in.items().stream().map(OrderParser::to).toList();

        return Order.builder()
            .accountId(in.accountId())
            .currency("BRL")
            .createdAt(LocalDateTime.now())
            .items(items)
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

        var items = o.items() == null ? List.<OrderItemOut>of() :
            o.items().stream().map(OrderParser::to).toList();

        return OrderOut.builder()
            .id(o.id())
            .createdAt(o.createdAt())
            .currency(o.currency())
            .total(o.total())
            .items(items)
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

    public static OrderModel toModel(Order o) {
        if (o == null) return null;

        OrderModel model = new OrderModel();
        model.accountId(o.accountId());
        model.createdAt(o.createdAt());
        model.currency(o.currency());
        model.total(o.total());
        return model;
    }

    public static Order to(OrderModel m) {
        if (m == null) return null;
        return Order.builder()
            .id(m.id())
            .accountId(m.accountId())
            .createdAt(m.createdAt())
            .currency(m.currency())
            .total(m.total())
            .build();
    }
}