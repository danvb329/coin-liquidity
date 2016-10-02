package com.coinliquidity.web.config;

import com.coinliquidity.core.ExchangeConfig;
import com.coinliquidity.web.LiquidityCache;
import com.coinliquidity.web.persist.FilePersister;
import com.coinliquidity.web.persist.LiquidityDataPersister;
import com.coinliquidity.web.rest.LiquidityController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("production")
public class LiquidityAppConfig {

    @Bean
    public LiquidityController liquidityController(final LiquidityCache liquidityCache) {
        return new LiquidityController(liquidityCache);
    }

    @Bean
    public LiquidityCache liquidityCache(final ExchangeConfig exchangeConfig,
                                         final LiquidityDataPersister liquidityDataPersister) {
        return new LiquidityCache(exchangeConfig, liquidityDataPersister);
    }

    @Bean
    public LiquidityDataPersister liquidityDataPersister() {
        final String dataDir = System.getProperty("liquidity.data.dir");
        if (dataDir == null) {
            throw new RuntimeException("Must specify liquidity.data.dir system property!");
        }
        return new FilePersister(dataDir);
    }

    @Bean
    public ExchangeConfig exchangeConfig() {
        return new ExchangeConfig("https://raw.githubusercontent.com/coin-liquidity/coin-liquidity-config/master/exchange.json");
    }
}
