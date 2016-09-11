package com.coinliquidity.web.config;

import com.coinliquidity.core.ExchangeConfig;
import com.coinliquidity.web.LiquidityCache;
import com.coinliquidity.web.rest.LiquidityController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LiquidityAppConfig {

    @Bean
    public LiquidityController liquidityController(final LiquidityCache liquidityCache) {
        return new LiquidityController(liquidityCache);
    }

    @Bean
    public LiquidityCache liquidityCache(final ExchangeConfig exchangeConfig) {
        return new LiquidityCache(exchangeConfig);
    }

    @Bean
    public ExchangeConfig exchangeConfig() {
        return new ExchangeConfig("config/exchange.json");
    }
}
