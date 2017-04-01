CREATE TABLE IF NOT EXISTS liquidity_history (
    run_date timestamp not null,
    exchange varchar(30) not null,
    base_ccy varchar(10) not null,
    quote_ccy varchar(10) not null,
    sell_cost decimal,
    buy_cost decimal,
    best_bid decimal,
    best_ask decimal,
    total_bids_usd decimal,
    total_asks decimal
);