SELECT
  base_ccy,
  sum(bids_1_usd) as bids_1,
  sum(bids_2_usd) as bids_2,
  sum(bids_3_usd) as bids_3,
  sum(bids_5_usd) as bids_5,
  sum(bids_10_usd) as bids_10,
  sum(bids_20_usd) as bids_20,
  sum(asks_1_usd) as asks_1,
  sum(asks_2_usd) as asks_2,
  sum(asks_3_usd) as asks_3,
  sum(asks_5_usd) as asks_5,
  sum(asks_10_usd) as asks_10,
  sum(asks_20_usd) as asks_20
FROM liquidity_history
WHERE run_date = (SELECT max(run_date) from liquidity_history) AND quote_ccy != 'BTC'
GROUP BY base_ccy