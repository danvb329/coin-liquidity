package com.coinliquidity.web.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
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
        return filter(datum -> datum.matches(baseCurrency, quoteCurrency));
    }

    public LiquidityData filter(final String exchange) {
        return filter(datum -> datum.matches(exchange));
    }

    private LiquidityData filter(final Predicate<LiquidityDatum> predicate) {
        final LiquidityData liquidityData = new LiquidityData();
        liquidityData.setUpdateTime(this.updateTime);
        liquidityData.setAmount(this.amount);
        final List<LiquidityDatum> filteredDatums = this.liquidityData.stream()
                .filter(predicate).collect(Collectors.toList());
        liquidityData.setLiquidityData(filteredDatums);
        return liquidityData;
    }
}
