package com.testingblaze.exception;

public class TestBlazeExceptionWithoutStackTrace extends Exception {
    public TestBlazeExceptionWithoutStackTrace(String message) {
        super(message, null, true, false);
    }
}
