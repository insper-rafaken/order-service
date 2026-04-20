package store.order;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductOut(UUID id, String name, BigDecimal price) {

}
