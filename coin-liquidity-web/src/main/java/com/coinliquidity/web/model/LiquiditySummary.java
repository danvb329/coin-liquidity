package com.coinliquidity.web.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class LiquiditySummary {

    private Instant updateTime;
    private BigDecimal totalBids;
    private BigDecimal totalAsks;
    private BigDecimal price;
}
