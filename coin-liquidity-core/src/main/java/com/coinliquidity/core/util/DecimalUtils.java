package com.coinliquidity.core.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DecimalUtils {

    private static final int PRICE_SCALE_SMALL = 5;
    private static final int PRICE_SCALE_LARGE = 2;

    private static final BigDecimal TWO = new BigDecimal(2);

    public static BigDecimal avgPrice(final BigDecimal price1, final BigDecimal price2) {
        return scalePrice(price1.add(price2).divide(TWO, PRICE_SCALE_SMALL, RoundingMode.HALF_UP));
    }

    public static BigDecimal scalePrice(final BigDecimal price) {
        if (price == null) {
            return null;
        }

        final int scale = price.compareTo(BigDecimal.ONE) >= 0 ? PRICE_SCALE_LARGE : PRICE_SCALE_SMALL;
        return price.setScale(scale, RoundingMode.HALF_UP);
    }

    public static BigDecimal inverseRate(final BigDecimal rate) {
        return BigDecimal.ONE.divide(rate, 10, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal toFraction(final int percent) {
        return new BigDecimal(percent).divide(new BigDecimal(100), 2, BigDecimal.ROUND_UNNECESSARY);
    }
}
