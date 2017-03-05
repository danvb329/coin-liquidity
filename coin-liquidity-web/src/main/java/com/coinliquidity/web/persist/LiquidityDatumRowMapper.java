package com.coinliquidity.web.persist;

import com.coinliquidity.core.model.CurrencyPair;
import com.coinliquidity.web.model.LiquidityDatum;
import org.springframework.jdbc.core.RowMapper;

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
        datum.setBestAsk(rs.getBigDecimal("best_ask"));
        datum.setBestBid(rs.getBigDecimal("best_bid"));
        datum.setBuyCost(rs.getBigDecimal("buy_cost"));
        datum.setSellCost(rs.getBigDecimal("sell_cost"));
        datum.setTotalAsks(rs.getBigDecimal("total_asks"));
        datum.setTotalBids(rs.getBigDecimal("total_bids_usd"));
        return datum;
    }
}
