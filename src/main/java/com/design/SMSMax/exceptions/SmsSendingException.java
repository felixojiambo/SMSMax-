package com.design.SMSMax.exceptions;
public class SmsSendingException extends RuntimeException {
    public SmsSendingException(String message, Throwable cause) {
        super(message, cause);
    }
}