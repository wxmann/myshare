package com.jimtang.myshare.calc;

/**
 * Created by tangz on 9/9/2015.
 */
class CumulativeShareResults extends ShareResults {

    public CumulativeShareResults(Double subtotal, Double tax, Double tip) {
        super("TOTAL", subtotal, tax, tip);
    }
}
