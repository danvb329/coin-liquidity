package com.coinliquidity.core.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DecimalUtils {

    private static final int PRICE_MIN_SCALE = 2;
    private static final int PRICE_MAX_SCALE = 8;

    private static final BigDecimal TWO = new BigDecimal(2);

    public static BigDecimal avgPrice(final BigDecimal price1, final BigDecimal price2) {
        return stripZeros(price1.add(price2).divide(TWO, PRICE_MAX_SCALE, RoundingMode.HALF_UP));
    }

    public static BigDecimal scalePrice(final BigDecimal price) {
        if (price == null) {
            return null;
        }

        return stripZeros(price.setScale(PRICE_MAX_SCALE, RoundingMode.HALF_UP));
    }

    public static BigDecimal convert(final BigDecimal price, final BigDecimal rate) {
        final int scale = Math.max(Math.min(rate.scale(), PRICE_MAX_SCALE), PRICE_MIN_SCALE);
        return price.divide(rate, scale, RoundingMode.HALF_UP);
    }

    public static BigDecimal inverseRate(final BigDecimal rate) {
        return BigDecimal.ONE.divide(rate, 10, BigDecimal.ROUND_HALF_UP).stripTrailingZeros();
    }

    public static BigDecimal toFraction(final int percent) {
        return new BigDecimal(percent).divide(new BigDecimal(100), 2, BigDecimal.ROUND_UNNECESSARY);
    }

    private static BigDecimal stripZeros(final BigDecimal d) {
        BigDecimal retVal = d.stripTrailingZeros();
        if (retVal.scale() < PRICE_MIN_SCALE) {
            retVal = retVal.setScale(PRICE_MIN_SCALE, RoundingMode.UNNECESSARY);
        }
        return retVal;
    }
}
