SELECT
run_date,
SUM(total_bids_usd) AS total_bids_usd,
SUM(total_asks) AS total_asks
FROM liquidity_history
WHERE base_ccy = ? AND run_date > ?
AND (0 = ? OR exchange = ?)
GROUP BY run_date
ORDER BY run_date