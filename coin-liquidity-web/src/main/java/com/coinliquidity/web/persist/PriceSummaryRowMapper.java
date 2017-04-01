package com.coinliquidity.web.persist;

import com.coinliquidity.web.model.PriceSummary;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PriceSummaryRowMapper implements RowMapper<PriceSummary> {

    @Override
    public PriceSummary mapRow(final ResultSet rs, final int i) throws SQLException {
        final PriceSummary summary = new PriceSummary();
        summary.setUpdateTime(rs.getTimestamp("run_date").toInstant());
        summary.setAvgAsk(rs.getBigDecimal("avg_ask"));
        summary.setAvgBid(rs.getBigDecimal("avg_bid"));
        return summary;
    }
}
