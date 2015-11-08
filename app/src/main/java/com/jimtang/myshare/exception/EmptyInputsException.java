package com.jimtang.myshare.exception;

/**
 * Thrown if input is empty/null when it shouldn't be.
 *
 * Created by tangz on 11/8/2015.
 */
public class EmptyInputsException extends RuntimeException {
    public EmptyInputsException() {
        super();
    }

    public EmptyInputsException(String detailMessage) {
        super(detailMessage);
    }

    public EmptyInputsException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public EmptyInputsException(Throwable throwable) {
        super(throwable);
    }
}
