package com.coinliquidity.web.persist;

import com.coinliquidity.web.model.LiquiditySummary;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LiquiditySummaryRowMapper implements RowMapper<LiquiditySummary> {

    @Override
    public LiquiditySummary mapRow(final ResultSet rs, final int i) throws SQLException {
        final LiquiditySummary summary = new LiquiditySummary();
        summary.setUpdateTime(rs.getTimestamp("run_date").toInstant());
        summary.setBaseCcy(rs.getString("base_ccy"));
        summary.setAvgAsk(rs.getBigDecimal("avg_ask"));
        summary.setAvgBid(rs.getBigDecimal("avg_bid"));
        summary.setTotalAsks(rs.getBigDecimal("total_asks"));
        summary.setTotalBidsUsd(rs.getBigDecimal("total_bids_usd"));
        return summary;
    }
}
