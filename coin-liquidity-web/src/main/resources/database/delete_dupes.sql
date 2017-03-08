DELETE FROM liquidity_history WHERE run_date IN
(
SELECT
run_date
FROM liquidity_history
GROUP BY run_date, exchange, base_ccy, quote_ccy
HAVING count(*) > 1
)