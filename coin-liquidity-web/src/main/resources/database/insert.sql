INSERT INTO liquidity_history (
    run_date,
    exchange,
    base_ccy,
    quote_ccy,
    best_bid,
    best_ask,
    price,

    bids_1,
    bids_1_usd,
    asks_1,
    asks_1_usd,

    bids_2,
    bids_2_usd,
    asks_2,
    asks_2_usd,

    bids_3,
    bids_3_usd,
    asks_3,
    asks_3_usd,

    bids_5,
    bids_5_usd,
    asks_5,
    asks_5_usd,

    bids_10,
    bids_10_usd,
    asks_10,
    asks_10_usd,

    bids_20,
    bids_20_usd,
    asks_20,
    asks_20_usd,

    total_bids,
    total_bids_usd,
    total_asks,
    total_asks_usd

) values (
?, ?, ?, ?, ?, ?, ?,
?, ?, ?, ?,
?, ?, ?, ?,
?, ?, ?, ?,
?, ?, ?, ?,
?, ?, ?, ?,
?, ?, ?, ?,
?, ?, ?, ?
)