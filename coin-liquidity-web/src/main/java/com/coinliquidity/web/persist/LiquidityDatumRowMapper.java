package com.coinliquidity.web.persist;

import com.coinliquidity.core.model.CurrencyPair;
import com.coinliquidity.web.model.LiquidityDatum;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.coinliquidity.core.analyzer.BidAskAnalyzer.PERCENTAGES;

public class LiquidityDatumRowMapper implements RowMapper<LiquidityDatum> {

    @Override
    public LiquidityDatum mapRow(final ResultSet rs, final int i) throws SQLException {
        final LiquidityDatum datum = new LiquidityDatum();
        datum.setUpdateTime(rs.getTimestamp("run_date").toInstant());
        datum.setExchange(rs.getString("exchange"));
        datum.setCurrencyPair(new CurrencyPair(
                rs.getString("base_ccy"),
                rs.getString("quote_ccy")));

        datum.setBestAsk(rs.getBigDecimal("best_ask"));
        datum.setBestBid(rs.getBigDecimal("best_bid"));

        for (final Integer percent : PERCENTAGES) {
            if (percent != 0) {
                datum.setAsksUsd(percent, rs.getBigDecimal("asks_" + percent + "_usd"));
                datum.setBidsUsd(percent, rs.getBigDecimal("bids_" + percent + "_usd"));
            }
        }

        datum.setAsks(0, rs.getBigDecimal("total_asks"));
        datum.setBids(0, rs.getBigDecimal("total_bids"));
        datum.setAsksUsd(0, rs.getBigDecimal("total_asks_usd"));
        datum.setBidsUsd(0, rs.getBigDecimal("total_bids_usd"));
        return datum;
    }
}
