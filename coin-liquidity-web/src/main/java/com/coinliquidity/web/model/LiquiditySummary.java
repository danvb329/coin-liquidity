package com.coinliquidity.web.model;

import com.google.common.collect.Maps;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

@Data
public class LiquiditySummary {

    private Instant updateTime;
    private BigDecimal totalBidsUsd;
    private BigDecimal totalAsks;
    private BigDecimal price;

    private final Map<Integer, BigDecimal> bids = Maps.newHashMap();
    private final Map<Integer, BigDecimal> asks = Maps.newHashMap();

    public BigDecimal getBids(final int percent) {
        return bids.get(percent);
    }

    public BigDecimal getAsks(final int percent) {
        return asks.get(percent);
    }

    public void setBids(final int percent, final BigDecimal value) {
        this.bids.put(percent, value);
    }

    public void setAsks(final int percent, final BigDecimal value) {
        this.asks.put(percent, value);
    }
}
