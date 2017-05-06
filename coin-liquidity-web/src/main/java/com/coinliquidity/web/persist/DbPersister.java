package com.coinliquidity.web.persist;

import com.coinliquidity.web.model.LiquidityData;
import com.coinliquidity.web.model.LiquidityDatum;
import com.coinliquidity.web.model.LiquiditySummary;
import com.coinliquidity.web.model.ViewType;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.coinliquidity.core.analyzer.BidAskAnalyzer.PERCENTAGES;
import static com.coinliquidity.core.util.DecimalUtils.avgPrice;
import static com.coinliquidity.core.util.DecimalUtils.percentDiff;
import static com.coinliquidity.core.util.ResourceUtils.resource;

public class DbPersister implements LiquidityDataPersister {

    private static final Logger LOGGER = LoggerFactory.getLogger(DbPersister.class);

    private static final LiquidityDatumRowMapper LIQUIDITY_DATUM_MAPPER = new LiquidityDatumRowMapper();
    private static final LiquiditySummaryRowMapper LIQUIDITY_SUMMARY_MAPPER = new LiquiditySummaryRowMapper();

    private static final String INSERT = resource("database/insert.sql");
    private static final String SELECT_LATEST = resource("database/select_latest.sql");
    private static final String SELECT_LIQUIDITY_SUMMARY = resource("database/select_liquidity_summary.sql");
    private static final String SELECT_PRICE_SUMMARY = resource("database/select_price_summary.sql");

    private final JdbcTemplate jdbcTemplate;

    @SneakyThrows
    public DbPersister(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.executeStartupScripts(Paths.get("db_scripts"));
    }

    @Override
    public void persist(final LiquidityData liquidityData) {
        LOGGER.info("persist()");
        final Stopwatch stopwatch = Stopwatch.createStarted();
        final List<Object[]> batchArgs = Lists.newArrayList();
        for (final LiquidityDatum datum : liquidityData.getLiquidityData()) {
            batchArgs.add(toArgs(liquidityData, datum));
        }
        jdbcTemplate.batchUpdate(INSERT, batchArgs);
        LOGGER.info("persist() took {}", stopwatch.stop());
    }

    @Override
    public Optional<LiquidityData> loadLatest() {
        LOGGER.info("loadLatest()");
        final Optional<LiquidityData> retVal;
        final Stopwatch stopwatch = Stopwatch.createStarted();
        final List<LiquidityDatum> datums = jdbcTemplate.query(SELECT_LATEST, LIQUIDITY_DATUM_MAPPER);
        if (!datums.isEmpty()) {
            final LiquidityData liquidityData = new LiquidityData();
            liquidityData.setUpdateTime(datums.get(0).getUpdateTime());
            liquidityData.setLiquidityData(datums);
            retVal = Optional.of(liquidityData);
        } else {
            retVal = Optional.empty();
        }
        LOGGER.info("loadLatest() took {}", stopwatch.stop());
        return retVal;
    }

    @Override
    public List<LiquiditySummary> loadSummary(final String baseCcy,
                                              final Instant threshold,
                                              final String exchange,
                                              final int bidAskPercent,
                                              final ViewType viewType) {
        LOGGER.info("loadSummary()");
        final Stopwatch stopwatch = Stopwatch.createStarted();

        final boolean exchangeFilter = exchange != null;
        final Object[] args = new Object[]{baseCcy, Timestamp.from(threshold), exchangeFilter, exchange};

        String bidsColumn;
        String asksColumn;

        if (bidAskPercent == 0) {
            bidsColumn = "total_bids";
            asksColumn = "total_asks";
        } else {
            bidsColumn = "bids_" + bidAskPercent;
            asksColumn = "asks_" + bidAskPercent;
        }

        if (ViewType.USD.equals(viewType)) {
            bidsColumn += "_usd";
            asksColumn += "_usd";
        } else if (ViewType.DEFAULT.equals(viewType)) {
            bidsColumn += "_usd";
        }

        final String sql = SELECT_LIQUIDITY_SUMMARY
                .replaceAll("<bids_column>", bidsColumn)
                .replaceAll("<asks_column>", asksColumn);

        final List<LiquiditySummary> liquiditySummaries = jdbcTemplate.query(sql, args, LIQUIDITY_SUMMARY_MAPPER);

        final Map<Instant, LiquiditySummary> map = liquiditySummaries.stream()
                .collect(Collectors.toMap(LiquiditySummary::getUpdateTime, Function.identity()));

        jdbcTemplate.query(SELECT_PRICE_SUMMARY, args, rs -> {
            final Instant runDate = rs.getTimestamp("run_date").toInstant();
            final BigDecimal avgBid = rs.getBigDecimal("avg_bid");
            final BigDecimal avgAsk = rs.getBigDecimal("avg_ask");

            final LiquiditySummary liquiditySummary = map.get(runDate);
            if (liquiditySummary != null) {
                if (BigDecimal.TEN.compareTo(percentDiff(avgBid, avgAsk)) > 0) {
                    liquiditySummary.setPrice(avgPrice(avgBid, avgAsk));
                }
            }
        });

        LOGGER.info("loadSummary() took {}", stopwatch.stop());
        return liquiditySummaries;
    }

    private Object[] toArgs(final LiquidityData liquidityData, final LiquidityDatum datum) {
        final List<Object> args = new ArrayList<>();
        args.add(Timestamp.from(liquidityData.getUpdateTime()));
        args.add(datum.getExchange());
        args.add(datum.getCurrencyPair().getBaseCurrency());
        args.add(datum.getCurrencyPair().getQuoteCurrency());
        args.add(datum.getBestBid());
        args.add(datum.getBestAsk());
        args.add(datum.getPrice());

        PERCENTAGES.forEach(percent -> {
            args.add(datum.getBids(percent));
            args.add(datum.getBidsUsd(percent));
            args.add(datum.getAsks(percent));
            args.add(datum.getAsksUsd(percent));
        });

        return args.toArray();
    }

    private void executeStartupScripts(final Path directory) throws IOException {
        if (directory.toFile().isDirectory()) {
            try (final DirectoryStream<Path> directoryStream = Files.newDirectoryStream(directory)) {
                for (final Path path : directoryStream) {
                    if (path.toString().endsWith(".sql")) {
                        executeScript(path);
                    }
                }
            }
        } else {
            LOGGER.warn("Script directory {} does not exist", directory);
        }
    }

    private void executeScript(final Path scriptName) throws IOException {
        final List<String> lines = Files.readAllLines(scriptName);

        final Stopwatch stopwatch = Stopwatch.createStarted();
        LOGGER.info("Executing {}, {} lines", scriptName, lines.size());

        try {
            lines.forEach(sql -> {
                final int count = jdbcTemplate.update(sql);
                LOGGER.info("Updated {} rows", count);
            });
        } catch (final Exception e) {
            LOGGER.error("Could not execute {}", scriptName, e);
        }

        LOGGER.info("Finished {} in {}", scriptName, stopwatch.stop());
    }
}
