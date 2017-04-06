package com.coinliquidity.web;

import com.coinliquidity.core.ExchangeConfig;
import com.coinliquidity.core.OrderBookDownloader;
import com.coinliquidity.core.analyzer.SlippageAnalyzer;
import com.coinliquidity.core.analyzer.TotalAnalyzer;
import com.coinliquidity.core.fx.FxRates;
import com.coinliquidity.core.model.DownloadStatus;
import com.coinliquidity.core.model.Exchanges;
import com.coinliquidity.core.model.OrderBook;
import com.coinliquidity.web.model.LiquidityData;
import com.coinliquidity.web.model.LiquidityDatum;
import com.coinliquidity.web.model.LiquiditySummary;
import com.coinliquidity.web.persist.LiquidityDataPersister;
import com.google.common.base.Stopwatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

public class LiquidityCache {

    private static final int THRESHOLD_DAYS = 1;

    private static final Logger LOGGER = LoggerFactory.getLogger(LiquidityCache.class);

    private final ExchangeConfig exchangeConfig;
    private final LiquidityDataPersister dataPersister;
    private final FxCache fxCache;

    private LiquidityData liquidityData;
    private Map<String, DownloadStatus> downloadStatuses;
    private List<LiquidityData> liquidityDataHistory;

    public LiquidityCache(final ExchangeConfig exchangeConfig,
                          final FxCache fxCache,
                          final LiquidityDataPersister dataPersister) {
        this.exchangeConfig = exchangeConfig;
        this.fxCache = fxCache;
        this.dataPersister = dataPersister;
        this.downloadStatuses = new ConcurrentSkipListMap<>();


        final Instant threshold = Instant.now().minus(THRESHOLD_DAYS, DAYS);
        this.liquidityDataHistory = dataPersister.loadHistory(threshold);

        final Optional<LiquidityData> latestData = dataPersister.loadLatest();

        if (latestData.isPresent()) {
            liquidityData = latestData.get();
        } else {
            refresh();
        }
    }

    @Scheduled(cron = "0 */10 * * * *")
    private void refresh() {
        final Exchanges exchanges = exchangeConfig.loadExchanges();

        final List<OrderBookDownloader> obds = exchanges.getExchangeList().stream()
                .map(OrderBookDownloader::new).collect(Collectors.toList());

        LOGGER.info("Creating pool of {} threads", obds.size());

        final ExecutorService executorService = Executors.newFixedThreadPool(obds.size());

        for (final OrderBookDownloader obd : obds) {
            executorService.execute(obd);
        }

        executorService.shutdown();

        do {
            LOGGER.debug("Awaiting termination");
            try {
                executorService.awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (!executorService.isTerminated());

        final List<OrderBook> orderBooks = new ArrayList<>();
        obds.forEach(obd -> orderBooks.addAll(obd.getOrderBooks()));

        final FxRates fxRates = fxCache.getRates();
        orderBooks.forEach(orderBook -> orderBook.convert(fxRates));

        this.liquidityData = toLiquidityData(orderBooks, new BigDecimal(100000));
        dataPersister.persist(this.liquidityData);
        updateHistory(liquidityData);

        final List<DownloadStatus> statuses = new ArrayList<>();
        obds.forEach(obd -> statuses.addAll(obd.getDownloadStatuses()));

        final Stopwatch now = Stopwatch.createStarted();

        statuses.forEach(status -> {
            final String key = status.getExchange() + "_" + status.getCurrencyPair();
            final DownloadStatus current = downloadStatuses.getOrDefault(key, status);

            current.setSizeBytes(status.getSizeBytes());
            current.setStatus(status.getStatus());
            current.setTimeElapsed(status.getTimeElapsed());
            current.setTotalAsks(status.getTotalAsks());
            current.setTotalBids(status.getTotalBids());
            current.setUpdateTime(status.getUpdateTime());

            if (DownloadStatus.OK.equals(status.getStatus())) {
                current.setLastOk(now);
            } else if (DownloadStatus.ERROR.equals(status.getStatus())) {
                current.setLastError(now);
                current.setLastErrorMessage(status.getLastErrorMessage());
            }

            downloadStatuses.put(key, current);
        });
    }

    private LiquidityData toLiquidityData(final List<OrderBook> orderBooks, final BigDecimal amount) {

        final List<LiquidityDatum> dataList = new ArrayList<>();
        orderBooks.forEach(orderBook -> {
            final SlippageAnalyzer slippageAnalyzer = new SlippageAnalyzer(amount);
            final TotalAnalyzer totalAnalyzer = new TotalAnalyzer();
            slippageAnalyzer.analyze(orderBook);
            totalAnalyzer.analyze(orderBook);

            final LiquidityDatum datum = new LiquidityDatum();
            datum.setExchange(orderBook.getName());
            datum.setCurrencyPair(orderBook.getOriginalCurrencyPair());
            datum.setBuyCost(slippageAnalyzer.getBuyCost());
            datum.setSellCost(slippageAnalyzer.getSellCost());
            datum.setBestAsk(scale(slippageAnalyzer.getBestAsk()));
            datum.setBestBid(scale(slippageAnalyzer.getBestBid()));
            datum.setTotalBids(totalAnalyzer.getTotalBids());
            datum.setTotalAsks(totalAnalyzer.getTotalAsks());
            dataList.add(datum);
        });

        final LiquidityData liquidityData = new LiquidityData();
        liquidityData.setLiquidityData(dataList);
        liquidityData.setUpdateTime(Instant.now());
        return liquidityData;
    }

    private BigDecimal scale(final BigDecimal decimal) {
        return decimal == null ? null : decimal.setScale(4, BigDecimal.ROUND_HALF_UP);
    }

    private void updateHistory(final LiquidityData liquidityData) {
        final Instant threshold = Instant.now().minus(THRESHOLD_DAYS, DAYS);
        liquidityDataHistory.add(liquidityData);
        liquidityDataHistory.removeIf(data -> data.getUpdateTime().isBefore(threshold));
    }

    public LiquidityData getLiquidityData() {
        return liquidityData;
    }

    public Collection<DownloadStatus> getDownloadStatuses() {
        return downloadStatuses.values();
    }

    public List<LiquidityData> getLiquidityDataHistory() {
        return liquidityDataHistory;
    }

    public List<LiquiditySummary> getLiquiditySummary(final String baseCcy, final Instant threshold) {
        return dataPersister.loadSummary(baseCcy, threshold);
    }

    public Set<String> getBaseCurrencies() {
        return liquidityData.getLiquidityData().stream()
                .map(LiquidityDatum::getBaseCurrency)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    public void validateBaseCcy(final String baseCcy) {
        if (!getBaseCurrencies().contains(baseCcy)) {
            throw new IllegalFilterException("Invalid base currency");
        }
    }
}
