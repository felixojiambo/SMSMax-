package com.design.SMSMax.services;

import com.design.SMSMax.exceptions.SmsSendingException;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SmsServiceImpl implements SmsService {

    private static final Logger logger = LoggerFactory.getLogger(SmsServiceImpl.class);

    // Example with Twilio
    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.phone.number}")
    private String fromPhoneNumber;

    @PostConstruct
    public void init() {
        Twilio.init(accountSid, authToken);
    }
    @Override
    @Retryable(
            value = { SmsSendingException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000, multiplier = 2))
    public boolean sendSms(String phoneNumber, String message) {
        try {
            // Existing SMS sending logic
            return true;
        } catch (Exception e) {
            throw new SmsSendingException("Failed to send SMS", e);
        }
    }

    @Recover
    public boolean recover(SmsSendingException e, String phoneNumber, String message) {
        // Handle recovery, e.g., log, notify, or send to dead-letter queue
        logger.error("Recovery after failed attempts to send SMS to {}", (Object) phoneNumber, Optional.ofNullable(e));
        return false;
    }
}
