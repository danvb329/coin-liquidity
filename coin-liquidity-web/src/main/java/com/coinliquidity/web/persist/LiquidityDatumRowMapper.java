package com.coinliquidity.web.persist;

import com.coinliquidity.core.model.CurrencyPair;
import com.coinliquidity.web.model.LiquidityDatum;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LiquidityDatumRowMapper implements RowMapper<LiquidityDatum> {

    @Override
    public LiquidityDatum mapRow(final ResultSet rs, final int i) throws SQLException {
        final LiquidityDatum datum = new LiquidityDatum();
        datum.setUpdateTime(rs.getTimestamp("run_date").toInstant());
        datum.setExchange(rs.getString("exchange"));
        datum.setCurrencyPair(new CurrencyPair(
                rs.getString("base_ccy"),
                rs.getString("quote_ccy")));

        datum.setBestAsk(scalePrice(rs.getBigDecimal("best_ask")));
        datum.setBestBid(scalePrice(rs.getBigDecimal("best_bid")));

        datum.setAsksUsd(1, rs.getBigDecimal("asks_1_usd"));
        datum.setBidsUsd(1, rs.getBigDecimal("bids_1_usd"));

        datum.setAsks(0, rs.getBigDecimal("total_asks"));
        datum.setBids(0, rs.getBigDecimal("total_bids"));
        datum.setAsksUsd(0, rs.getBigDecimal("total_asks_usd"));
        datum.setBidsUsd(0, rs.getBigDecimal("total_bids_usd"));
        return datum;
    }

    private BigDecimal scalePrice(final BigDecimal price) {
        if (price == null) {
            return null;
        }

        // 2 decimals if price >= 1
        if (price.compareTo(BigDecimal.ONE) >= 0) {
            return price.setScale(2, RoundingMode.HALF_UP);
        } else {
            return price;
        }
    }
}
