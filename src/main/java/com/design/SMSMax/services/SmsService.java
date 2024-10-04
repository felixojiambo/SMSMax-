package com.design.SMSMax.services;
public interface SmsService {
    boolean sendSms(String phoneNumber, String message);
}