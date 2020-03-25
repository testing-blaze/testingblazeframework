package com.testblaze.exception;

public class TestBlazeExceptionWithoutStackTrace extends Exception {
    public TestBlazeExceptionWithoutStackTrace(String message) {
        super(message, null, true, false);
    }
}
