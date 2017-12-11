INSERT INTO coin_data (
  run_date,
  id,
  name,
  symbol,
  price_usd,
  price_btc,
  volume_24h_usd,
  market_cap_usd,
  available_supply,
  total_supply,
  max_supply,
  last_updated)
VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);