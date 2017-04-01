SELECT
run_date,
sum(total_bids_usd) as total_bids_usd,
sum(total_asks) as total_asks
FROM liquidity_history
WHERE base_ccy = ? and run_date > ?
GROUP BY run_date
ORDER BY run_date