DROP TABLE IF EXISTS stock_items;
DROP TABLE IF EXISTS stock_history;
DROP TABLE IF EXISTS stock_rollback_history;

CREATE TABLE stock_items
(
    id     BIGSERIAL        NOT NULL,
    name   VARCHAR(255)     NOT NULL,
    amount INT              NOT NULL,
    price  DOUBLE PRECISION NOT NULL,
    CONSTRAINT PK_stock_items PRIMARY KEY (id)
);

CREATE TABLE stock_history
(
    id          BIGSERIAL NOT NULL,
    customer_id BIGSERIAL NOT NULL,
    order_id    BIGSERIAL NOT NULL,
    item_id     BIGSERIAL NOT NULL,
    amount      INT       NOT NULL,
    CONSTRAINT PK_stock_history PRIMARY KEY (id)
);

CREATE TABLE stock_rollback_history
(
    id          BIGSERIAL NOT NULL,
    customer_id BIGSERIAL NOT NULL,
    order_id    BIGSERIAL NOT NULL,
    CONSTRAINT PK_stock_rollback_history PRIMARY KEY (id)
);
