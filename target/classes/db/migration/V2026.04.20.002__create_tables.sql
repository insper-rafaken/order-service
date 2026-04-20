CREATE TABLE orders.orders (
    id          VARCHAR(36)    PRIMARY KEY,
    account_id  VARCHAR(36)    NOT NULL,
    currency    VARCHAR(10)    NOT NULL DEFAULT 'BRL',
    total       NUMERIC(15, 2) NOT NULL DEFAULT 0,
    created_at  TIMESTAMP      NOT NULL DEFAULT NOW()
);

CREATE TABLE orders.order_items (
    id          VARCHAR(36)    PRIMARY KEY,
    order_id    VARCHAR(36)    NOT NULL REFERENCES orders.orders(id) ON DELETE CASCADE,
    product_id  VARCHAR(36)    NOT NULL,
    quantity    INTEGER        NOT NULL,
    price       NUMERIC(15, 2) NOT NULL
);