package com.design.SMSMax.dtos;

import lombok.Data;
import org.hibernate.annotations.processing.Pattern;
@Data
public class SmsRequest {

    @NotBlank
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number")
    private String phoneNumber;

    @NotBlank
    @Size(max = 160, message = "Message too long")
    private String message;

}