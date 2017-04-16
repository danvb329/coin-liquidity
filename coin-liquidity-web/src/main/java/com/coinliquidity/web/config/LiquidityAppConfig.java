package com.coinliquidity.web.config;

import com.coinliquidity.core.ExchangeConfig;
import com.coinliquidity.core.fx.BitcoinAverageProvider;
import com.coinliquidity.core.fx.FixerIoProvider;
import com.coinliquidity.core.fx.FxProvider;
import com.coinliquidity.web.FxCache;
import com.coinliquidity.web.LiquidityCache;
import com.coinliquidity.web.persist.DbPersister;
import com.coinliquidity.web.persist.LiquidityDataPersister;
import com.coinliquidity.web.rest.DataController;
import com.coinliquidity.web.rest.ViewController;
import com.coinliquidity.web.rest.StatusController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class LiquidityAppConfig {

    private static final String BASE_CCY = "USD";

    @Bean
    public ViewController viewController(final LiquidityCache liquidityCache) {
        return new ViewController(liquidityCache);
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
                                         final FxCache fxCache,
                                         final LiquidityDataPersister liquidityDataPersister) {
        return new LiquidityCache(exchangeConfig, fxCache, liquidityDataPersister);
    }

    @Bean
    public FxCache fxCache() {
        final List<FxProvider> fxProviders = new ArrayList<>();
        fxProviders.add(new FixerIoProvider(BASE_CCY));
        fxProviders.add(new BitcoinAverageProvider(BASE_CCY));
        return new FxCache(fxProviders);
    }

    @Bean
    public LiquidityDataPersister liquidityDataPersister(final JdbcTemplate jdbcTemplate) {
        return new DbPersister(jdbcTemplate);
    }
}
