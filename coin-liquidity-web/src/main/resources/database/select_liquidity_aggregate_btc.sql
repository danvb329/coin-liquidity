SELECT
  base_ccy,
  sum(bids_1) as bids_1,
  sum(bids_2) as bids_2,
  sum(bids_3) as bids_3,
  sum(bids_5) as bids_5,
  sum(bids_10) as bids_10,
  sum(bids_20) as bids_20,
  sum(asks_1) as asks_1,
  sum(asks_2) as asks_2,
  sum(asks_3) as asks_3,
  sum(asks_5) as asks_5,
  sum(asks_10) as asks_10,
  sum(asks_20) as asks_20
FROM liquidity_history
WHERE run_date = (SELECT max(run_date) from liquidity_history) AND quote_ccy = 'BTC'
GROUP BY base_ccy