package com.coinliquidity.web.persist;

import com.coinliquidity.web.model.LiquidityData;
import com.coinliquidity.web.model.LiquiditySummary;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface LiquidityDataPersister {

    void persist(final LiquidityData liquidityData);

    Optional<LiquidityData> loadLatest();

    List<LiquidityData> loadHistory(final Instant threshold);

    List<LiquiditySummary> loadSummary(final String baseCcy, final Instant threshold, final String exchange);
}
