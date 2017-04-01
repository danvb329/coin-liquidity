SELECT
    run_date,
    (AVG(best_bid) + AVG(best_ask)) / 2 AS avg_price
FROM liquidity_history
WHERE base_ccy = ? AND run_date > ?
AND exchange IN ('gdax.com', 'gemini.com', 'poloniex.com', 'bitfinex.com', 'bitstamp.com')
GROUP BY run_date