package com.coinliquidity.web;

import com.coinliquidity.core.coindata.CoinDataDownloader;
import com.coinliquidity.core.model.CoinDatum;
import com.coinliquidity.web.persist.CoinDataDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

public class CoinDataCache {

    private static final Logger LOGGER = LoggerFactory.getLogger(CoinDataCache.class);

    private final CoinDataDownloader coinDataDownloader;
    private final CoinDataDao coinDataDao;

    public CoinDataCache(final CoinDataDownloader coinDataDownloader,
                         final CoinDataDao coinDataDao) {
        this.coinDataDownloader = coinDataDownloader;
        this.coinDataDao = coinDataDao;
    }

    @Scheduled(cron = "0 1 * * * *")
    public void updateCoinData() {
        final List<CoinDatum> latest = coinDataDao.getLatestCoinData();

        final Instant now = LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC);

        if (latest.isEmpty() || now.isAfter(latest.get(0).getLastUpdated())) {
            LOGGER.info("Saving data for {}", now);
            final List<CoinDatum> coinData = coinDataDownloader.downloadData(now);
            coinDataDao.persisCoinData(coinData);
        } else {
            LOGGER.info("Already saved data for {}", now);
        }
    }

}
