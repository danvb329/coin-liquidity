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
//        final String dataDir = System.getProperty("liquidity.data.dir");
//        if (dataDir == null) {
//            throw new RuntimeException("Must specify liquidity.data.dir system property!");
//        }
//        final FilePersister filePersister = new FilePersister(dataDir);
//        List<LiquidityData> fileHistory = filePersister.loadHistory(Instant.now().minus(60, DAYS));
//
//        final DbPersister dbPersister = new DbPersister(jdbcTemplate);
//        fileHistory.forEach(dbPersister::persist);
//        return dbPersister;
        return new DbPersister(jdbcTemplate);
    }

    @Bean
    public ExchangeConfig exchangeConfig() {
        return new ExchangeConfig("https://raw.githubusercontent.com/coin-liquidity/coin-liquidity-config/master/exchange.json");
    }
}
