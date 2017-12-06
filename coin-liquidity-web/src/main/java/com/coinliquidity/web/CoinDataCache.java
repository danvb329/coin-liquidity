package com.coinliquidity.web;

import com.coinliquidity.core.coindata.CoinDataDownloader;
import com.coinliquidity.core.model.CoinDatum;
import com.coinliquidity.web.persist.CoinDataDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.coinliquidity.core.util.DecimalUtils.anyNull;
import static com.coinliquidity.core.util.DecimalUtils.percent;
import static java.time.temporal.ChronoUnit.DAYS;

public class CoinDataCache {

    private static final int INFLATION_DAYS = 1;

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
        final Instant maxDate = coinDataDao.getMaxDate();
        final Instant now = LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC);

        if (maxDate == null || now.isAfter(maxDate)) {
            LOGGER.info("Saving data for {}", now);
            final List<CoinDatum> coinData = coinDataDownloader.downloadData(now);
            coinDataDao.persisCoinData(coinData);
        } else {
            LOGGER.info("Already saved data for {}", now);
        }
    }

    public List<CoinDatum> getCoinData() {
        final List<CoinDatum> currentData = coinDataDao.getLatestCoinData();
        final Instant currentDate = currentData.get(0).getRunDate();
        final Instant priorDate = currentDate.minus(INFLATION_DAYS, DAYS);
        final List<CoinDatum> priorData = coinDataDao.getHistoricalCoinData(priorDate);
        final Map<String, CoinDatum> priorMap = priorData.stream()
                .collect(Collectors.toMap(CoinDatum::getSymbol, Function.identity()));

        for (final CoinDatum currentDatum : currentData) {
            if (currentDatum.getMaxSupply() != null) {
                currentDatum.setPercentMined(percent(currentDatum.getAvailableSupply(), currentDatum.getMaxSupply(), 1));
            } else {
                currentDatum.setPercentMined(BigDecimal.ZERO);
            }

            final CoinDatum priorDatum = priorMap.get(currentDatum.getSymbol());
            if (priorDatum != null) {
                currentDatum.setInflation(calculateInflation(currentDatum, priorDatum, INFLATION_DAYS));
            }
        }

        return currentData;
    }

    BigDecimal calculateInflation(final CoinDatum current, final CoinDatum prior, final int days) {
        final BigDecimal currentSupply = current.getAvailableSupply();
        final BigDecimal priorSupply = prior.getAvailableSupply();

        if (anyNull(currentSupply, priorSupply)) {
            return null;
        }

        final BigDecimal increase = currentSupply.subtract(priorSupply);
        final BigDecimal percentIncrease = percent(increase, priorSupply, 8);
        return percentIncrease.multiply(BigDecimal.valueOf(365)).divide(BigDecimal.valueOf(days), 1, RoundingMode.HALF_UP);
    }

}
