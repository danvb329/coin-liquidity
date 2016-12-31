package com.coinliquidity.web.persist;

import com.coinliquidity.web.model.LiquidityData;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface LiquidityDataPersister {

    void persist(final LiquidityData liquidityData);

    Optional<LiquidityData> loadLatest();

    List<LiquidityData> loadHistory(final Instant threshold);
}
