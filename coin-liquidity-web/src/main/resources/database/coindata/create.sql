CREATE TABLE IF NOT EXISTS coin_data (
    run_date timestamp not null,
    id varchar(255) not null,
    name varchar(255) not null,
    symbol varchar(50) not null,
    price_usd decimal,
    price_btc decimal,
    volume_24h_usd decimal,
    market_cap_usd decimal,
    available_supply decimal,
    total_supply decimal,
    max_supply decimal,
    last_updated timestamp,
    PRIMARY KEY(run_date, id)
);