package com.design.SMSMax.services;

import com.design.SMSMax.models.SmsMessage;
import com.design.SMSMax.models.Status;
import com.design.SMSMax.repository.SmsMessageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class KafkaConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);

    @Autowired
    private SmsService smsService;

    @Autowired
    private SmsMessageRepository smsMessageRepository;

    @KafkaListener(topics = "sms_send_requests", groupId = "sms-group")
    public void consume(String message) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            SmsMessage smsMessage = mapper.readValue(message, SmsMessage.class);
            logger.info("Consumed message: {}", smsMessage);

            // Send SMS via SMS Service
            boolean isSent = smsService.sendSms(smsMessage.getPhoneNumber(), smsMessage.getMessage());

            // Update status
            smsMessage.setStatus(isSent ? Status.SENT : Status.FAILED);
            smsMessage.setUpdatedAt(LocalDateTime.now());
            smsMessageRepository.save(smsMessage);

        } catch (Exception e) {
            logger.error("Error processing message: {}", message, e);
            // Implement retry or send to dead-letter queue
        }
    }
}
