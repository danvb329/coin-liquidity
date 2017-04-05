package com.coinliquidity.web.config;

import com.coinliquidity.core.ExchangeConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("production")
public class ProductionConfig {
    @Bean
    public ExchangeConfig exchangeConfig() {
        return new ExchangeConfig("https://raw.githubusercontent.com/coin-liquidity/coin-liquidity-config/master/exchange-local.json");
    }
}
