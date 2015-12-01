package com.jimtang.myshare.exception;

/**
 * Created by tangz on 12/1/2015.
 */
public class DataRetrievalException extends RuntimeException {
    public DataRetrievalException() {
    }

    public DataRetrievalException(String detailMessage) {
        super(detailMessage);
    }

    public DataRetrievalException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public DataRetrievalException(Throwable throwable) {
        super(throwable);
    }
}
