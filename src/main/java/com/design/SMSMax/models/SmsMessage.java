package com.design.SMSMax.models;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
@Entity
@Table(name = "sms_messages")
@Data
public class SmsMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String phoneNumber;

    @Column(nullable=false, length=160)
    private String message;

    @Enumerated(EnumType.STRING)
    private Status status;

    private int retryCount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}