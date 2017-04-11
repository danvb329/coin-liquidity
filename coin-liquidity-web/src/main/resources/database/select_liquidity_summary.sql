SELECT
run_date,
price,
SUM(<bids_column>) AS total_bids_usd,
SUM(<asks_column>) AS total_asks
FROM liquidity_history
WHERE base_ccy = ? AND run_date > ?
AND (0 = ? OR exchange = ?)
GROUP BY run_date
ORDER BY run_date