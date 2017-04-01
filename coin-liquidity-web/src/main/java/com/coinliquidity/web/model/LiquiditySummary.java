package com.coinliquidity.web.model;

import java.math.BigDecimal;
import java.time.Instant;

public class LiquiditySummary {

    private Instant updateTime;
    private BigDecimal totalBidsUsd;
    private BigDecimal totalAsks;
    private BigDecimal price;

    public Instant getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
