package com.coinliquidity.core.fx;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

public class FxRates {

    private final String baseCcy;
    private final Instant updateTime;
    private final Map<String, BigDecimal> rates;
    private final Map<String, BigDecimal> inverseRates;

    FxRates(final String baseCcy,
            final Instant updateTime,
            final Map<String, BigDecimal> rates,
            final Map<String, BigDecimal> inverseRates) {
        this.baseCcy = baseCcy;
        this.updateTime = updateTime;
        this.rates = rates;
        this.inverseRates = inverseRates;
    }

    public BigDecimal getRate(final String ccy) {
        if (baseCcy.equals(ccy)) {
            return BigDecimal.ONE;
        }
        return rates.get(ccy);
    }

    public BigDecimal getInverseRate(final String ccy) {
        if (baseCcy.equals(ccy)) {
            return BigDecimal.ONE;
        }
        return inverseRates.get(ccy);
    }

    public void merge(final FxRates other) {
        this.rates.putAll(other.rates);
        this.inverseRates.putAll(other.inverseRates);
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
