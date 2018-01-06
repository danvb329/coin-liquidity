package com.coinliquidity.web.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LiquidityAggregate {

    private String baseCcy;

    private BigDecimal bids1;
    private BigDecimal asks1;
    private BigDecimal bids2;
    private BigDecimal asks2;
    private BigDecimal bids3;
    private BigDecimal asks3;
    private BigDecimal bids5;
    private BigDecimal asks5;
    private BigDecimal bids10;
    private BigDecimal asks10;
    private BigDecimal bids20;
    private BigDecimal asks20;

}
