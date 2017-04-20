DROP INDEX IF EXISTS liquidity_history_idx_2;
CREATE INDEX IF NOT EXISTS liquidity_history_idx ON liquidity_history (run_date, base_ccy);