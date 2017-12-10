package com.coinliquidity.web.persist;

import com.coinliquidity.core.model.CoinDatum;
import lombok.SneakyThrows;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;

public class CoinDataMapper implements RowMapper<CoinDatum> {

    @Override
    @SneakyThrows
    public CoinDatum mapRow(final ResultSet rs, final int i) {
        final CoinDatum coinDatum = new CoinDatum();
        coinDatum.setRunDate(rs.getTimestamp("run_date").toInstant());
        coinDatum.setName(rs.getString("name"));
        coinDatum.setSymbol(rs.getString("symbol"));
        coinDatum.setPriceUsd(rs.getBigDecimal("price_usd"));
        coinDatum.setPriceBtc(rs.getBigDecimal("price_btc"));
        coinDatum.setVolume24hUsd(rs.getBigDecimal("volume_24h_usd"));
        coinDatum.setMarketCapUsd(rs.getBigDecimal("market_cap_usd"));
        coinDatum.setAvailableSupply(rs.getBigDecimal("available_supply"));
        coinDatum.setTotalSupply(rs.getBigDecimal("total_supply"));
        coinDatum.setMaxSupply(rs.getBigDecimal("max_supply"));
        coinDatum.setLastUpdated(rs.getTimestamp("last_updated").toInstant());
        return coinDatum;
    }
}
