package com.coinliquidity.web.config;

import com.coinliquidity.core.ExchangeConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("local")
public class LocalConfig {
    @Bean
    public ExchangeConfig exchangeConfig() {
        return new ExchangeConfig("exchange-local.json");
    }
}
