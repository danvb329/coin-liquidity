CREATE TABLE IF NOT EXISTS coin_data_2 (
    run_date timestamp not null,
    id varchar(255) not null,
    name varchar(255) not null,
    symbol varchar(10) not null,
    price_usd decimal,
    price_btc decimal,
    volume_24h_usd decimal,
    market_cap_usd decimal,
    available_supply decimal,
    total_supply decimal,
    max_supply decimal,
    last_updated timestamp,
    PRIMARY KEY(id, symbol)
);