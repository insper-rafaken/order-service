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
            .total(0.0)
            .items(items)
            .build();
    }

    public static OrderItem to(OrderItemIn in) {
        if (in == null) return null;
        return OrderItem.builder()
            .productId(in.productId())
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
            .productId(item.productId())
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
        model.setAccountId(o.accountId());
        model.setCreatedAt(o.createdAt());
        model.setCurrency(o.currency());
        model.setTotal(o.total());

        List<OrderItemModel> items = o.items() == null ? List.of() :
            o.items().stream().map(item -> toItemModel(item, model)).toList();
        model.setItems(items);

        return model;
    }

    public static OrderItemModel toItemModel(OrderItem item, OrderModel order) {
        if (item == null) return null;
        OrderItemModel m = new OrderItemModel();
        m.setOrder(order);
        m.setProductId(item.productId());
        m.setQuantity(item.quantity());
        m.setPrice(item.price());
        return m;
    }

    public static Order to(OrderModel m) {
        if (m == null) return null;

        List<OrderItem> items = m.getItems() == null ? List.of() :
            m.getItems().stream().map(OrderParser::to).toList();

        return Order.builder()
            .id(m.getId())
            .accountId(m.getAccountId())
            .createdAt(m.getCreatedAt())
            .currency(m.getCurrency())
            .total(m.getTotal())
            .items(items)
            .build();
    }

    public static OrderItem to(OrderItemModel m) {
        if (m == null) return null;
        return OrderItem.builder()
            .id(m.getId())
            .productId(m.getProductId())
            .quantity(m.getQuantity())
            .price(m.getPrice())
            .build();
    }
}
