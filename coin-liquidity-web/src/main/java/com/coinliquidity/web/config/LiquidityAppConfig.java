package com.coinliquidity.web.config;

import com.coinliquidity.core.ExchangeConfig;
import com.coinliquidity.web.LiquidityCache;
import com.coinliquidity.web.persist.DbPersister;
import com.coinliquidity.web.persist.LiquidityDataPersister;
import com.coinliquidity.web.rest.DataController;
import com.coinliquidity.web.rest.LiquidityController;
import com.coinliquidity.web.rest.StatusController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@Profile("production")
public class LiquidityAppConfig {

    @Bean
    public LiquidityController liquidityController(final LiquidityCache liquidityCache) {
        return new LiquidityController(liquidityCache);
    }

    @Bean
    public DataController dataController(final LiquidityCache liquidityCache) {
        return new DataController(liquidityCache);
    }

    @Bean
    public StatusController statusController(final LiquidityCache liquidityCache) {
        return new StatusController(liquidityCache);
    }

    @Bean
    public LiquidityCache liquidityCache(final ExchangeConfig exchangeConfig,
                                         final LiquidityDataPersister liquidityDataPersister) {
        return new LiquidityCache(exchangeConfig, liquidityDataPersister);
    }

    @Bean
    public LiquidityDataPersister liquidityDataPersister(final JdbcTemplate jdbcTemplate) {
        return new DbPersister(jdbcTemplate, Boolean.getBoolean("delete.dupes"));
    }

    @Bean
    public ExchangeConfig exchangeConfig() {
        return new ExchangeConfig("https://raw.githubusercontent.com/coin-liquidity/coin-liquidity-config/master/exchange.json");
    }
}
