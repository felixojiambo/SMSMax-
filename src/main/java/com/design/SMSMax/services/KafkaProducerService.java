package com.design.SMSMax.services;

import com.design.SMSMax.models.SmsMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);
    private static final String TOPIC = "sms_send_requests";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(SmsMessage smsMessage) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String message = mapper.writeValueAsString(smsMessage);
            kafkaTemplate.send(TOPIC, message);
            logger.info("Sent message: {}", message);
        } catch (JsonProcessingException e) {
            logger.error("Error serializing SMS message", e);
        }
    }
}