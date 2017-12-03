package com.coinliquidity.web.config;

import com.coinliquidity.core.coindata.CoinDataDownloader;
import com.coinliquidity.core.util.HttpClient;
import com.coinliquidity.web.CoinDataCache;
import com.coinliquidity.web.persist.CoinDataDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class CoinDataConfig {

    @Bean
    public CoinDataCache coinDataCache(final CoinDataDownloader coinDataDownloader,
                                       final CoinDataDao coinDataDao) {
        return new CoinDataCache(coinDataDownloader, coinDataDao);
    }

    @Bean
    public CoinDataDownloader coinDataDownloader(final HttpClient httpClient) {
        return new CoinDataDownloader("https://api.coinmarketcap.com/v1/ticker", httpClient);
    }

    @Bean
    public CoinDataDao coinDataDao(final JdbcTemplate jdbcTemplate) {
        return new CoinDataDao(jdbcTemplate);
    }
}
