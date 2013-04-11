package org.monkey.common.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalUtils {

    public static BigDecimal roundHalfUpTo2DP(BigDecimal input) {
        return input.setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal roundHalfUpTo2DP(Double input) {
        return roundHalfUpTo2DP(BigDecimal.valueOf(input));
    }


}
