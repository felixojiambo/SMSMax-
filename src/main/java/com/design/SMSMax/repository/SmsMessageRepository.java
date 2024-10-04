package com.design.SMSMax.repository;

import com.design.SMSMax.models.SmsMessage;
import com.design.SMSMax.models.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SmsMessageRepository extends JpaRepository<SmsMessage, Long> {
    List<SmsMessage> findByStatus(Status status);
}