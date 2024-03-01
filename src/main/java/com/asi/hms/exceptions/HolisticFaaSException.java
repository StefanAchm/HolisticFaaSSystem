package com.asi.hms.exceptions;

public class HolisticFaaSException extends Exception {

    public HolisticFaaSException(String message) {
        super(message);
    }

    public HolisticFaaSException(String message, Throwable cause) {
        super(message, cause);
    }

}
