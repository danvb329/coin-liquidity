package com.coinliquidity.web;

import com.coinliquidity.core.ExchangeConfig;
import com.coinliquidity.core.OrderBookDownloader;
import com.coinliquidity.core.analyzer.BidAskAnalyzer;
import com.coinliquidity.core.download.HttpDownloader;
import com.coinliquidity.core.fx.FxRates;
import com.coinliquidity.core.model.Exchanges;
import com.coinliquidity.core.model.OrderBook;
import com.coinliquidity.web.model.*;
import com.coinliquidity.web.persist.LiquidityAggregateDao;
import com.coinliquidity.web.persist.LiquidityDataPersister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.coinliquidity.core.analyzer.BidAskAnalyzer.PERCENTAGES;
import static com.coinliquidity.core.util.DecimalUtils.scalePrice;

public class LiquidityCache {

    private static final Logger LOGGER = LoggerFactory.getLogger(LiquidityCache.class);

    private final ExchangeConfig exchangeConfig;
    private final LiquidityDataPersister dataPersister;
    private final FxCache fxCache;
    private final HttpDownloader httpDownloader;
    private final StatusCache statusCache;
    private final LiquidityAggregateDao liquidityAggregateDao;

    private LiquidityData liquidityData;


    public LiquidityCache(final ExchangeConfig exchangeConfig,
                          final FxCache fxCache,
                          final LiquidityDataPersister dataPersister,
                          final HttpDownloader httpDownloader,
                          final StatusCache statusCache, LiquidityAggregateDao liquidityAggregateDao) {
        this.exchangeConfig = exchangeConfig;
        this.fxCache = fxCache;
        this.dataPersister = dataPersister;
        this.httpDownloader = httpDownloader;
        this.statusCache = statusCache;
        this.liquidityAggregateDao = liquidityAggregateDao;

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

        final List<OrderBookDownloader> downloaders = exchanges.getExchangeList().stream()
                .map(exchange -> new OrderBookDownloader(exchange, httpDownloader))
                .collect(Collectors.toList());

        this.download(downloaders);

        final List<OrderBook> orderBooks = downloaders.stream()
                .map(OrderBookDownloader::getOrderBooks)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        // convert all to same base CCY
        final FxRates fxRates = fxCache.getRates();
        orderBooks.forEach(orderBook -> orderBook.convert(fxRates));

        this.liquidityData = toLiquidityData(orderBooks);
        this.dataPersister.persist(this.liquidityData);

        statusCache.update(downloaders);
    }

    private void download(final List<OrderBookDownloader> downloaders) {
        final int threads = Math.min(downloaders.size(), 20);
        LOGGER.info("Creating pool of {} threads", threads);

        final ExecutorService executorService = Executors.newFixedThreadPool(threads);
        downloaders.forEach(executorService::execute);

        executorService.shutdown();

        do {
            LOGGER.debug("Awaiting termination");
            try {
                executorService.awaitTermination(5, TimeUnit.SECONDS);
            } catch (final InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        } while (!executorService.isTerminated());
    }

    private LiquidityData toLiquidityData(final List<OrderBook> orderBooks) {
        final List<LiquidityDatum> dataList = orderBooks.stream()
                .map(orderBook -> {
                    final LiquidityDatum datum = new LiquidityDatum();
                    datum.setExchange(orderBook.getName());
                    datum.setCurrencyPair(orderBook.getOriginalCurrencyPair());
                    datum.setBestAsk(scalePrice(orderBook.getAsks().getBestPrice()));
                    datum.setBestBid(scalePrice(orderBook.getBids().getBestPrice()));
                    datum.setPrice(orderBook.getMidPrice());
                    PERCENTAGES.forEach(percent -> analyzeBidsAsks(orderBook, datum, percent));
                    return datum;
                }).collect(Collectors.toList());

        final LiquidityData liquidityData = new LiquidityData();
        liquidityData.setLiquidityData(dataList);
        liquidityData.setUpdateTime(Instant.now());
        return liquidityData;
    }

    private void analyzeBidsAsks(final OrderBook orderBook,
                                 final LiquidityDatum datum,
                                 final int percent) {
        final BidAskAnalyzer analyzer = new BidAskAnalyzer(percent);
        analyzer.analyze(orderBook);
        datum.setBids(percent, analyzer.getTotalBids());
        datum.setAsks(percent, analyzer.getTotalAsks());
        datum.setBidsUsd(percent, analyzer.getTotalBidsUsd());
        datum.setAsksUsd(percent, analyzer.getTotalAsksUsd());
    }

    public LiquidityData getLiquidityData() {
        return liquidityData;
    }

    public List<LiquiditySummary> getLiquiditySummary(final String baseCcy,
                                                      final Instant threshold,
                                                      final String exchange,
                                                      final int bidAskPercent,
                                                      final ViewType viewType) {
        return dataPersister.loadSummary(baseCcy, threshold, exchange, bidAskPercent, viewType);
    }

    public Set<String> getBaseCurrencies() {
        return liquidityData.getLiquidityData().stream()
                .map(LiquidityDatum::getBaseCurrency)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    public Set<String> getExchanges() {
        final TreeSet<String> exchanges = liquidityData.getLiquidityData().stream()
                .map(LiquidityDatum::getExchange)
                .collect(Collectors.toCollection(TreeSet::new));
        exchanges.add("all");
        return exchanges;
    }

    public void validateBaseCcy(final String baseCcy) {
        if (!getBaseCurrencies().contains(baseCcy)) {
            throw new IllegalFilterException("Invalid base currency");
        }
    }

    public void validateExchange(final String exchange) {
        if (!"all".equals(exchange) && !getExchanges().contains(exchange)) {
            throw new IllegalFilterException("Invalid exchange");
        }
    }

    public void validateBidAskPercent(final int bidAskPercent) {
        if (!(bidAskPercent == 0 || PERCENTAGES.contains(bidAskPercent))) {
            throw new IllegalFilterException("Invalid bidAskPercent");
        }
    }

    public List<LiquidityAggregate> getLiquidityAggregates(final String type) {
        if (!type.equals("fiat") && !type.equals("btc")) {
            throw new IllegalFilterException("Invalid type. Must be either fiat or btc.");
        }
        return liquidityAggregateDao.getLatest(type);
    }
}
