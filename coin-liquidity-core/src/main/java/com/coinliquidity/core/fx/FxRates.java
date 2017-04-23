package com.coinliquidity.core.fx;

import com.google.common.collect.Maps;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

import static com.coinliquidity.core.util.DecimalUtils.inverseRate;

public class FxRates {

    private final String baseCcy;
    private final Instant updateTime;
    private final Map<String, BigDecimal> rates;
    private final Map<String, BigDecimal> inverseRates;

    FxRates(final String baseCcy,
            final Instant updateTime) {
        this.baseCcy = baseCcy;
        this.updateTime = updateTime;
        this.rates = Maps.newHashMap();
        this.inverseRates = Maps.newHashMap();
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

    FxRates putRate(final String ccy, final BigDecimal rate) {
        rates.put(ccy, rate);
        inverseRates.put(ccy, inverseRate(rate));
        return this;
    }

    FxRates putInverseRate(final String ccy, final BigDecimal inverseRate) {
        inverseRates.put(ccy, inverseRate);
        rates.put(ccy, inverseRate(inverseRate));
        return this;
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
