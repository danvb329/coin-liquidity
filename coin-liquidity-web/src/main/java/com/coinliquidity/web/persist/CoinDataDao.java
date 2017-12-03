package com.coinliquidity.web.persist;

import com.coinliquidity.core.model.CoinDatum;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcOperations;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static com.coinliquidity.core.util.ResourceUtils.resource;

public class CoinDataDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(CoinDataDao.class);

    private static final CoinDataMapper MAPPER = new CoinDataMapper();
    private static final String SELECT_LATEST = resource("database/coindata/select_latest.sql");
    private static final String INSERT = resource("database/coindata/insert.sql");
    private static final String CREATE = resource("database/coindata/create.sql");

    private final JdbcOperations jdbc;

    public CoinDataDao(final JdbcOperations jdbc) {
        this.jdbc = jdbc;
        jdbc.execute(CREATE);
    }

    public List<CoinDatum> getLatestCoinData() {
        LOGGER.info("getLatestCoinData()");
        final Stopwatch stopwatch = Stopwatch.createStarted();
        final List<CoinDatum> coinData = jdbc.query(SELECT_LATEST, MAPPER);
        LOGGER.info("getLatestCoinData() took {}", stopwatch.stop());
        return coinData;
    }

    public void persisCoinData(final List<CoinDatum> coinData) {
        LOGGER.info("persisCoinData()");
        final Stopwatch stopwatch = Stopwatch.createStarted();
        final List<Object[]> batchArgs = new ArrayList<>(coinData.size());
        for (final CoinDatum coinDatum : coinData) {
            batchArgs.add(toArgs(coinDatum));
        }
        jdbc.batchUpdate(INSERT, batchArgs);
        LOGGER.info("persisCoinData() took {}", stopwatch.stop());
    }

    private Object[] toArgs(final CoinDatum coinDatum) {
        final List<Object> args = Lists.newArrayList();
        args.add(Timestamp.from(coinDatum.getRunDate()));
        args.add(coinDatum.getSymbol());
        args.add(coinDatum.getPriceUsd());
        args.add(coinDatum.getPriceBtc());
        args.add(coinDatum.getVolume24hUsd());
        args.add(coinDatum.getMarketCapUsd());
        args.add(coinDatum.getAvailableSupply());
        args.add(coinDatum.getTotalSupply());
        args.add(coinDatum.getMaxSupply());
        args.add(Timestamp.from(coinDatum.getLastUpdated()));
        return args.toArray();
    }
}
