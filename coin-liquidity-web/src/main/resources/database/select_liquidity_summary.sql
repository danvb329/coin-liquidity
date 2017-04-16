SELECT
run_date,
SUM(<bids_column>) AS total_bids,
SUM(<asks_column>) AS total_asks
FROM liquidity_history
WHERE base_ccy = ? AND run_date > ?
AND (0 = ? OR exchange = ?)
GROUP BY run_date
ORDER BY run_date