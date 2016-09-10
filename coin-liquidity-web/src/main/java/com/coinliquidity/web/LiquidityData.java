package com.coinliquidity.web;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
}
