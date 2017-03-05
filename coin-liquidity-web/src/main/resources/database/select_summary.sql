SELECT
run_date,
base_ccy,
avg(best_bid) as avg_bid,
avg(best_ask) as avg_ask,
sum(total_bids_usd) as total_bids_usd,
sum(total_asks) as total_asks
FROM liquidity_history
WHERE base_ccy = ? and run_date > ?
GROUP BY run_date, base_ccy
ORDER BY run_date, base_ccy