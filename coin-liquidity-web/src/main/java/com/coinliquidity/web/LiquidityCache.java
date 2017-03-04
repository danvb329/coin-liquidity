package com.coinliquidity.web;

import com.coinliquidity.core.ExchangeConfig;
import com.coinliquidity.core.FxConverter;
import com.coinliquidity.core.OrderBookDownloader;
import com.coinliquidity.core.analyzer.SlippageAnalyzer;
import com.coinliquidity.core.analyzer.TotalAnalyzer;
import com.coinliquidity.core.model.DownloadStatus;
import com.coinliquidity.core.model.Exchanges;
import com.coinliquidity.core.model.OrderBook;
import com.coinliquidity.web.model.LiquidityData;
import com.coinliquidity.web.model.LiquidityDatum;
import com.coinliquidity.web.persist.LiquidityDataPersister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

public class LiquidityCache {

    private static final int THREHOLD_DAYS = 10;

    private static final Logger LOGGER = LoggerFactory.getLogger(LiquidityCache.class);

    private final ExchangeConfig exchangeConfig;
    private final LiquidityDataPersister dataPersister;

    private LiquidityData liquidityData;
    private List<DownloadStatus> downloadStatuses;
    private List<LiquidityData> liquidityDataHistory;

    public LiquidityCache(final ExchangeConfig exchangeConfig, LiquidityDataPersister dataPersister) {
        this.exchangeConfig = exchangeConfig;
        this.dataPersister = dataPersister;
        this.downloadStatuses = new ArrayList<>();


        final Instant threshold = Instant.now().minus(THREHOLD_DAYS, DAYS);
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

        final FxConverter fxConverter = new FxConverter("USD");
        orderBooks.forEach(orderBook -> orderBook.convert(fxConverter));

        this.liquidityData = toLiquidityData(orderBooks, new BigDecimal(100000));
        dataPersister.persist(this.liquidityData);
        updateHistory(liquidityData);

        final List<DownloadStatus> statuses = new ArrayList<>();
        obds.forEach(obd -> statuses.addAll(obd.getDownloadStatuses()));
        this.downloadStatuses = statuses;
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
        return decimal == null ? null : decimal.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    private void updateHistory(final LiquidityData liquidityData) {
        final Instant threshold = Instant.now().minus(THREHOLD_DAYS, DAYS);
        liquidityDataHistory.add(liquidityData);
        liquidityDataHistory.removeIf(data -> data.getUpdateTime().isBefore(threshold));
    }

    public LiquidityData getLiquidityData() {
        return liquidityData;
    }

    public List<DownloadStatus> getDownloadStatuses() {
        return downloadStatuses;
    }

    public List<LiquidityData> getLiquidityDataHistory() {
        return liquidityDataHistory;
    }
}
