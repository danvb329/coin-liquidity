package com.coinliquidity.web;

import com.coinliquidity.core.ExchangeConfig;
import com.coinliquidity.core.FxConverter;
import com.coinliquidity.core.OrderBookDownloader;
import com.coinliquidity.core.analyzer.SlippageAnalyzer;
import com.coinliquidity.core.analyzer.TotalAnalyzer;
import com.coinliquidity.core.model.Exchanges;
import com.coinliquidity.core.model.OrderBook;
import com.coinliquidity.web.model.LiquidityData;
import com.coinliquidity.web.model.LiquidityDatum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class LiquidityCache {

    private static final Logger LOGGER = LoggerFactory.getLogger(LiquidityCache.class);

    private final ExchangeConfig exchangeConfig;

    private LiquidityData liquidityData;

    public LiquidityCache(final ExchangeConfig exchangeConfig) {
        this.exchangeConfig = exchangeConfig;
        refresh();
    }

    @Scheduled(initialDelay = 600000, fixedDelay = 600000)
    public void refresh() {
        final Exchanges exchanges = exchangeConfig.loadExchanges();

        final List<OrderBookDownloader> obds = exchanges.getExchangeList().stream()
                .map(OrderBookDownloader::new).collect(Collectors.toList());

        final ExecutorService executorService = Executors.newFixedThreadPool(10);

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
        obds.forEach(orderBookDownloader -> orderBooks.addAll(orderBookDownloader.getOrderBooks()));

        final FxConverter fxConverter = new FxConverter("USD");
        orderBooks.forEach(orderBook -> orderBook.convert(fxConverter));

        this.liquidityData = toLiquidityData(orderBooks, new BigDecimal(100000));
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
            datum.setBestAsk(slippageAnalyzer.getBestAsk().setScale(2, BigDecimal.ROUND_HALF_UP));
            datum.setBestBid(slippageAnalyzer.getBestBid().setScale(2, BigDecimal.ROUND_HALF_UP));
            datum.setTotalBids(totalAnalyzer.getTotalBids());
            datum.setTotalAsks(totalAnalyzer.getTotalAsks());
            dataList.add(datum);
        });

        final LiquidityData liquidityData = new LiquidityData();
        liquidityData.setAmount(amount);
        liquidityData.setLiquidityData(dataList);
        liquidityData.setUpdateTime(new Date());
        return liquidityData;
    }

    public LiquidityData getLiquidityData() {
        return liquidityData;
    }
}
