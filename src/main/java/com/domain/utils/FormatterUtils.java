package com.domain.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** Helper class provides basic property manipulation methods. */
public final class FormatterUtils {

    public static BigDecimal formatToBigDecimal(int value) {
        return setHalfEvenRoundingMode(new BigDecimal(value));
    }

    public static BigDecimal setHalfEvenRoundingMode(BigDecimal value) {
        return value.setScale(4, RoundingMode.HALF_EVEN);
    }
}
