SELECT * FROM coin_data WHERE run_date = (SELECT max(run_date) from coin_data)