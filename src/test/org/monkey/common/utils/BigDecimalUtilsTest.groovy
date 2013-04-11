package org.monkey.common.utils

import org.junit.Test

import static java.math.BigDecimal.valueOf
import static org.monkey.common.utils.BigDecimalUtils.roundHalfUpTo2DP


class BigDecimalUtilsTest {

    @Test
    public void roundHalfUpTo2DP() {
        assert roundHalfUpTo2DP(valueOf(12.345)) == 12.35
        assert roundHalfUpTo2DP(valueOf(12.344)) == 12.34
        assert roundHalfUpTo2DP(valueOf(12)) == 12.00
    }
}
