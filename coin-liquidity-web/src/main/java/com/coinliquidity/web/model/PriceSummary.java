package com.coinliquidity.web.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;

public class PriceSummary {

    private static final BigDecimal TWO = new BigDecimal("2");

    private Instant updateTime;
    private BigDecimal avgBid;
    private BigDecimal avgAsk;

    public Instant getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }

    public void setAvgBid(BigDecimal avgBid) {
        this.avgBid = avgBid;
    }

    public void setAvgAsk(BigDecimal avgAsk) {
        this.avgAsk = avgAsk;
    }

    public BigDecimal getMidBidAsk() {
        return avgAsk.add(avgBid).divide(TWO, 4, RoundingMode.HALF_UP);
    }
}
