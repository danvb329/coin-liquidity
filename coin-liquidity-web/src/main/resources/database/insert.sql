INSERT INTO liquidity_history (
    run_date,
    exchange,
    base_ccy,
    quote_ccy,
    sell_cost,
    buy_cost,
    best_bid,
    best_ask,
    total_bids_usd,
    total_asks
    )
values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)