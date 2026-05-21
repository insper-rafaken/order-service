package store.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OrderParser {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public static Order to(OrderIn in, String idAccount) {
        if (in == null) return null;

        var items = in.items() == null ? List.<OrderItem>of() :
            in.items().stream().map(OrderParser::to).toList();

        return Order.builder()
            .accountId(idAccount)
            .currency("BRL")
            .createdAt(LocalDateTime.now())
            .total(BigDecimal.ZERO)
            .status("PENDING")
            .items(items)
            .build();
    }

    public static OrderItem to(OrderItemIn in) {
        if (in == null) return null;
        return OrderItem.builder()
            .productId(in.idProduct())
            .quantity(in.quantity())
            .price(BigDecimal.ZERO)
            .build();
    }

    public static OrderOut to(Order o) {
        if (o == null) return null;

        var items = o.items() == null ? List.<OrderItemOut>of() :
            o.items().stream().map(OrderParser::to).toList();

        return OrderOut.builder()
            .id(o.id())
            .date(o.createdAt() != null ? o.createdAt().format(DATE_FMT) : null)
            .total(o.total())
            .items(items)
            .currency(o.currency())
            .status(o.status())
            .build();
    }

    public static OrderItemOut to(OrderItem item) {
        if (item == null) return null;
        BigDecimal total = item.price().multiply(BigDecimal.valueOf(item.quantity()));
        return OrderItemOut.builder()
            .id(item.id())
            .product(OrderItemOut.Product.builder().id(item.productId()).build())
            .quantity(item.quantity())
            .total(total)
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
        model.setStatus(o.status() != null ? o.status() : "PENDING");

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
            .status(m.getStatus())
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
