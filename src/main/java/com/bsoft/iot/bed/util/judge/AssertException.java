package com.bsoft.iot.bed.util.judge;

/**
 * @author zzx
 */
public class AssertException extends RuntimeException {

    public AssertException(String message) {
        super(message);
    }

    public AssertException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
