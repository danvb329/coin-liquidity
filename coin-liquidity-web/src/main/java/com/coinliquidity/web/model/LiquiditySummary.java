package com.coinliquidity.web.model;

import java.math.BigDecimal;
import java.time.Instant;

public class LiquiditySummary {

    private Instant updateTime;
    private String baseCcy;
    private BigDecimal avgBid;
    private BigDecimal avgAsk;
    private BigDecimal totalBidsUsd;
    private BigDecimal totalAsks;

    public Instant getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }

    public String getBaseCcy() {
        return baseCcy;
    }

    public void setBaseCcy(String baseCcy) {
        this.baseCcy = baseCcy;
    }

    public BigDecimal getAvgBid() {
        return avgBid;
    }

    public void setAvgBid(BigDecimal avgBid) {
        this.avgBid = avgBid;
    }

    public BigDecimal getAvgAsk() {
        return avgAsk;
    }

    public void setAvgAsk(BigDecimal avgAsk) {
        this.avgAsk = avgAsk;
    }

    public BigDecimal getTotalBidsUsd() {
        return totalBidsUsd;
    }

    public void setTotalBidsUsd(BigDecimal totalBidsUsd) {
        this.totalBidsUsd = totalBidsUsd;
    }

    public BigDecimal getTotalAsks() {
        return totalAsks;
    }

    public void setTotalAsks(BigDecimal totalAsks) {
        this.totalAsks = totalAsks;
    }
}
