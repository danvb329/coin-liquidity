package com.coinliquidity.web.config;

import com.coinliquidity.core.ExchangeConfig;
import com.coinliquidity.core.download.HttpDownloader;
import com.coinliquidity.core.fx.BitcoinAverageProvider;
import com.coinliquidity.core.fx.FixerIoProvider;
import com.coinliquidity.core.fx.FxProvider;
import com.coinliquidity.core.fx.KrakenProvider;
import com.coinliquidity.core.model.CurrencyPair;
import com.coinliquidity.core.util.HttpClient;
import com.coinliquidity.web.FxCache;
import com.coinliquidity.web.LiquidityCache;
import com.coinliquidity.web.StatusCache;
import com.coinliquidity.web.persist.DbPersister;
import com.coinliquidity.web.persist.LiquidityAggregateDao;
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

    private static final String BASE_CCY = CurrencyPair.USD;

    @Bean
    public ViewController viewController(final LiquidityCache liquidityCache) {
        return new ViewController(liquidityCache);
    }

    @Bean
    public DataController dataController(final LiquidityCache liquidityCache) {
        return new DataController(liquidityCache);
    }

    @Bean
    public StatusController statusController(final StatusCache liquidityCache) {
        return new StatusController(liquidityCache);
    }

    @Bean
    public StatusCache statusCache() {
        return new StatusCache();
    }

    @Bean
    public LiquidityCache liquidityCache(final ExchangeConfig exchangeConfig,
                                         final FxCache fxCache,
                                         final LiquidityDataPersister liquidityDataPersister,
                                         final HttpDownloader httpDownloader,
                                         final StatusCache statusCache,
                                         final LiquidityAggregateDao liquidityAggregateDao) {
        return new LiquidityCache(exchangeConfig,
                fxCache, liquidityDataPersister, httpDownloader, statusCache, liquidityAggregateDao);
    }

    @Bean
    public FxCache fxCache(final HttpClient httpClient) {
        final List<FxProvider> fxProviders = new ArrayList<>();
        fxProviders.add(new FixerIoProvider(httpClient, BASE_CCY));
        fxProviders.add(new BitcoinAverageProvider(httpClient, BASE_CCY));
        fxProviders.add(new KrakenProvider(httpClient));
        return new FxCache(fxProviders);
    }

    @Bean
    public LiquidityDataPersister liquidityDataPersister(final JdbcTemplate jdbcTemplate) {
        return new DbPersister(jdbcTemplate);
    }

    @Bean
    public LiquidityAggregateDao liquidityAggregateDao(final JdbcTemplate jdbcTemplate) {
        return new LiquidityAggregateDao(jdbcTemplate);
    }

    @Bean
    public HttpDownloader httpDownloader(final HttpClient httpClient) {
        return new HttpDownloader(httpClient);
    }

    @Bean
    public HttpClient httpClient() {
        return new HttpClient();
    }
}
