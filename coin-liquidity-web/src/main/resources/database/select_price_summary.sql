SELECT
    run_date,
    AVG(best_bid) AS avg_bid,
    AVG(best_ask) AS avg_ask
FROM liquidity_history
WHERE base_ccy = ? AND run_date > ?
AND exchange IN ('gdax.com', 'gemini.com', 'poloniex.com', 'bitfinex.com', 'bitstamp.net')
GROUP BY run_date