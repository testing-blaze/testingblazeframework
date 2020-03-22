package com.fast.exception;

public class FastExceptionWithoutStackTrace extends Exception {
    public FastExceptionWithoutStackTrace(String message) {
        super(message, null, true, false);
    }
}
