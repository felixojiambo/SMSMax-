package com.design.SMSMax.controllers;

import com.design.SMSMax.dtos.ApiResponse;
import com.design.SMSMax.dtos.SmsRequest;
import com.design.SMSMax.models.SmsMessage;
import com.design.SMSMax.models.Status;
import com.design.SMSMax.repository.SmsMessageRepository;
import com.design.SMSMax.services.KafkaProducerService;
import com.design.SMSMax.services.RateLimited;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/sms")

public class SmsController {

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    private SmsMessageRepository smsMessageRepository;

    @PostMapping("/send")
    @RateLimited
    public ResponseEntity<?> sendSms(@Valid @RequestBody SmsRequest smsRequest) {
        // Create SMS Message entity
        SmsMessage smsMessage = new SmsMessage();
        smsMessage.setPhoneNumber(smsRequest.getPhoneNumber());
        smsMessage.setMessage(smsRequest.getMessage());
        smsMessage.setStatus(Status.PENDING);
        smsMessage.setCreatedAt(LocalDateTime.now());

        // Save to DB
        smsMessage = smsMessageRepository.save(smsMessage);

        // Send to Kafka
        kafkaProducerService.sendMessage(smsMessage);

        return ResponseEntity.ok(new ApiResponse(true, "SMS queued successfully", smsMessage.getId()));
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<?> getSmsStatus(@PathVariable Long id) {
        Optional<SmsMessage> smsMessage = smsMessageRepository.findById(id);
        if (!smsMessage.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "SMS message not found", null));
        }
        return ResponseEntity.ok(smsMessage.get());
    }
}
