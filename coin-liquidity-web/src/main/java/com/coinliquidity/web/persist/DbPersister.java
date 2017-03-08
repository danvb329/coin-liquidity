package com.coinliquidity.web.persist;

import com.coinliquidity.web.model.LiquidityData;
import com.coinliquidity.web.model.LiquidityDatum;
import com.coinliquidity.web.model.LiquiditySummary;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class DbPersister implements LiquidityDataPersister {

    private static final LiquidityDatumRowMapper LIQUIDITY_DATUM_ROW_MAPPER = new LiquidityDatumRowMapper();
    private static final LiquiditySummaryRowMapper LIQUIDITY_SUMMARY_ROW_MAPPER = new LiquiditySummaryRowMapper();

    private static final String CREATE = resource("database/create.sql");
    private static final String INSERT = resource("database/insert.sql");
    private static final String SELECT_SINCE = resource("database/select_since.sql");
    private static final String SELECT_LATEST = resource("database/select_latest.sql");
    private static final String SELECT_SUMMARY = resource("database/select_summary.sql");
    private static final String DELETE_DUPES = resource("database/delete_dupes.sql");

    private final JdbcTemplate jdbcTemplate;

    public DbPersister(final JdbcTemplate jdbcTemplate, final boolean deleteDupes) {
        this.jdbcTemplate = jdbcTemplate;
        jdbcTemplate.execute(CREATE);

        if (deleteDupes) {
            jdbcTemplate.execute(DELETE_DUPES);
        }
    }

    @Override
    public void persist(final LiquidityData liquidityData) {
        final List<Object[]> batchArgs = Lists.newArrayList();
        for (final LiquidityDatum datum : liquidityData.getLiquidityData()) {
            batchArgs.add(new Object[] {
                    Timestamp.from(liquidityData.getUpdateTime()),
                    datum.getExchange(),
                    datum.getCurrencyPair().getBaseCurrency(),
                    datum.getCurrencyPair().getQuoteCurrency(),
                    datum.getSellCost(),
                    datum.getBuyCost(),
                    datum.getBestBid(),
                    datum.getBestAsk(),
                    datum.getTotalBids(),
                    datum.getTotalAsks()
            });
        }
        jdbcTemplate.batchUpdate(INSERT, batchArgs);
    }

    @Override
    public Optional<LiquidityData> loadLatest() {
        final List<LiquidityDatum> datums = jdbcTemplate.query(SELECT_LATEST, LIQUIDITY_DATUM_ROW_MAPPER);
        if (!datums.isEmpty()) {
            final LiquidityData liquidityData = new LiquidityData();
            liquidityData.setUpdateTime(datums.get(0).getUpdateTime());
            liquidityData.setLiquidityData(datums);
            return Optional.of(liquidityData);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<LiquidityData> loadHistory(final Instant threshold) {
        final List<LiquidityDatum> datums = jdbcTemplate.query(SELECT_SINCE,
                new Object[] { Timestamp.from(threshold) }, LIQUIDITY_DATUM_ROW_MAPPER);

        final Map<Instant, List<LiquidityDatum>> grouped =
                new TreeMap<>(datums.stream().collect(Collectors.groupingBy(LiquidityDatum::getUpdateTime)));

        return grouped.entrySet().stream().map(entry -> {
            final LiquidityData liquidityData = new LiquidityData();
            liquidityData.setUpdateTime(entry.getKey());
            liquidityData.setLiquidityData(entry.getValue());
            return liquidityData;
        }).collect(Collectors.toList());
    }

    @Override
    public List<LiquiditySummary> loadSummary(final String baseCcy, final Instant threshold) {
        return jdbcTemplate.query(SELECT_SUMMARY,
                new Object[] { baseCcy, Timestamp.from(threshold) }, LIQUIDITY_SUMMARY_ROW_MAPPER);
    }

    private static String resource(final String name) {
        try {
            return Resources.toString(Resources.getResource(name), Charsets.UTF_8);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}
