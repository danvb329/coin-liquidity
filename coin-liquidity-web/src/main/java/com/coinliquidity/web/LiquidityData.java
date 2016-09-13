package com.coinliquidity.web;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class LiquidityData {

    private List<LiquidityDatum> liquidityData;
    private Date updateTime;
    private BigDecimal amount;

    public List<LiquidityDatum> getLiquidityData() {
        return liquidityData;
    }

    public void setLiquidityData(final List<LiquidityDatum> liquidityData) {
        this.liquidityData = liquidityData;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(final Date updateTime) {
        this.updateTime = updateTime;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(final BigDecimal amount) {
        this.amount = amount;
    }

    public LiquidityData filter(final String baseCurrency, final String quoteCurrency) {
        final LiquidityData liquidityData = new LiquidityData();
        liquidityData.setUpdateTime(this.updateTime);
        liquidityData.setAmount(this.amount);
        final List<LiquidityDatum> filteredDatums = this.liquidityData.stream()
                .filter(d -> d.matches(baseCurrency, quoteCurrency)).collect(Collectors.toList());
        liquidityData.setLiquidityData(filteredDatums);
        return liquidityData;
    }

    public LiquidityData filter(String exchange) {
        final LiquidityData liquidityData = new LiquidityData();
        liquidityData.setUpdateTime(this.updateTime);
        liquidityData.setAmount(this.amount);
        final List<LiquidityDatum> filteredDatums = this.liquidityData.stream()
                .filter(d -> d.matches(exchange)).collect(Collectors.toList());
        liquidityData.setLiquidityData(filteredDatums);
        return liquidityData;
    }
}
