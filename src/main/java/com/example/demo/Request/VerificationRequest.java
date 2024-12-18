package com.example.demo.Request;

import lombok.Data;

@Data
public class VerificationRequest {
    private String email;
    private String code;

    // Getters and setters
}
