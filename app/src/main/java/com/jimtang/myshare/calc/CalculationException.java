package com.jimtang.myshare.calc;

/**
 * Created by tangz on 10/19/2015.
 */
public class CalculationException extends RuntimeException {

    public CalculationException() {
        super();
    }

    public CalculationException(String detailMessage) {
        super(detailMessage);
    }

    public CalculationException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public CalculationException(Throwable throwable) {
        super(throwable);
    }
}
