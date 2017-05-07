package com.coinliquidity.core.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Objects;

public class DecimalUtils {

    private static final int PRICE_MIN_SCALE = 2;
    private static final int PRICE_MAX_SCALE = 8;

    public static final BigDecimal TWO = new BigDecimal(2);
    public static final BigDecimal HUNDRED = new BigDecimal(100);
    public static final BigDecimal MAX = new BigDecimal(Long.MAX_VALUE);

    public static BigDecimal avgPrice(final BigDecimal price1, final BigDecimal price2) {
        if (anyNull(price1, price2)) {
            return null;
        }
        return stripZeros(price1.add(price2).divide(TWO, PRICE_MAX_SCALE, RoundingMode.HALF_UP));
    }

    public static BigDecimal scalePrice(final BigDecimal price) {
        if (price == null) {
            return null;
        }

        // 2 decimals if price >= 1
        if (price.compareTo(BigDecimal.ONE) >= 0) {
            return stripZeros(price.setScale(PRICE_MIN_SCALE, RoundingMode.HALF_UP));
        } else {
            return price;
        }
    }

    public static BigDecimal convert(final BigDecimal price, final BigDecimal rate) {
        final int scale = Math.max(Math.min(rate.scale(), PRICE_MAX_SCALE), PRICE_MIN_SCALE);
        return price.divide(rate, scale, RoundingMode.HALF_UP);
    }

    public static BigDecimal inverseRate(final BigDecimal rate) {
        return BigDecimal.ONE.divide(rate, 10, RoundingMode.HALF_UP).stripTrailingZeros();
    }

    public static BigDecimal toFraction(final int percent) {
        return new BigDecimal(percent).divide(HUNDRED, 2, RoundingMode.UNNECESSARY);
    }

    public static BigDecimal percentDiff(final BigDecimal d1, final BigDecimal d2) {
        if (anyNull(d1, d2)) {
            return MAX;
        }

        final BigDecimal diff = d1.subtract(d2).abs();
        return diff.multiply(HUNDRED).divide(d1, 0, RoundingMode.HALF_UP);
    }

    static boolean anyNull(BigDecimal... decimals) {
        return Arrays.stream(decimals).anyMatch(Objects::isNull);
    }

    private static BigDecimal stripZeros(final BigDecimal d) {
        BigDecimal retVal = d.stripTrailingZeros();
        if (retVal.scale() < PRICE_MIN_SCALE) {
            retVal = retVal.setScale(PRICE_MIN_SCALE, RoundingMode.UNNECESSARY);
        }
        return retVal;
    }
}
