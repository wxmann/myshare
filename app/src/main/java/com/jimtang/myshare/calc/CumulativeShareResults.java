package com.jimtang.myshare.calc;

import java.math.BigDecimal;

/**
 * Created by tangz on 9/9/2015.
 */
class CumulativeShareResults extends ShareResults {

    public CumulativeShareResults(BigDecimal subtotal, BigDecimal tax, BigDecimal tip) {
        super("TOTAL", subtotal, tax, tip);
    }
}
