package com.coinliquidity.core.fx;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

public class FxRates {

    private final String baseCcy;
    private final Instant updateTime;
    private final Map<String, BigDecimal> rates;

    FxRates(final String baseCcy, final Instant updateTime, final Map<String, BigDecimal> rates) {
        this.baseCcy = baseCcy;
        this.updateTime = updateTime;
        this.rates = rates;
    }

    public BigDecimal getRate(final String ccy) {
        if (baseCcy.equals(ccy)) {
            return BigDecimal.ONE;
        }
        return rates.get(ccy);
    }

    public void merge(final FxRates other) {
        this.rates.putAll(other.rates);
    }

    public String getBaseCcy() {
        return baseCcy;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    @Override
    public String toString() {
        return "FxRates{" +
                "baseCcy='" + baseCcy + '\'' +
                ", updateTime=" + updateTime +
                ", rates=" + rates +
                '}';
    }
}
