package com.coinliquidity.web.persist;

import com.coinliquidity.web.model.LiquidityAggregate;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static com.coinliquidity.core.util.ResourceUtils.resource;

public class LiquidityAggregateDao {

    private static final String SELECT_AGGREGATE_FIAT = resource("database/select_liquidity_aggregate_fiat.sql");
    private static final String SELECT_AGGREGATE_BTC = resource("database/select_liquidity_aggregate_btc.sql");

    private final JdbcTemplate jdbcTemplate;

    public LiquidityAggregateDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<LiquidityAggregate> getLatest(final String type) {
        final String select = "fiat".equals(type) ? SELECT_AGGREGATE_FIAT : SELECT_AGGREGATE_BTC;
        final List<LiquidityAggregate> aggregates = jdbcTemplate.query(select, (rs, rowNum) -> {
            final LiquidityAggregate aggregate = new LiquidityAggregate();
            aggregate.setBaseCcy(rs.getString("base_ccy"));
            aggregate.setBids1(rs.getBigDecimal("bids_1"));
            aggregate.setAsks1(rs.getBigDecimal("asks_1"));
            aggregate.setBids2(rs.getBigDecimal("bids_2"));
            aggregate.setAsks2(rs.getBigDecimal("asks_2"));
            aggregate.setBids3(rs.getBigDecimal("bids_3"));
            aggregate.setAsks3(rs.getBigDecimal("asks_3"));
            aggregate.setBids5(rs.getBigDecimal("bids_5"));
            aggregate.setAsks5(rs.getBigDecimal("asks_5"));
            aggregate.setBids10(rs.getBigDecimal("bids_10"));
            aggregate.setAsks10(rs.getBigDecimal("asks_10"));
            aggregate.setBids20(rs.getBigDecimal("bids_20"));
            aggregate.setAsks20(rs.getBigDecimal("asks_20"));
            return aggregate;
        });
        return aggregates;
    }

}
