DROP TABLE IF EXISTS items;

CREATE TABLE items
(
    id BIGSERIAL           NOT NULL,
    name VARCHAR(255)      NOT NULL,
    amount INT             NOT NULL,
    price DOUBLE PRECISION NOT NULL,
    CONSTRAINT PK_items PRIMARY KEY (id)
);