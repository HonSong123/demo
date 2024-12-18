package com.example.demo.Service;

import java.util.HashMap;
import java.util.Random;

import org.springframework.stereotype.Service;

@Service
public class VerificationService {

    private final HashMap<String, String> verificationCodes = new HashMap<>();

    // Generate a verification code and store it against the email
    public String generateVerificationCode(String email) {
        String code = String.format("%06d", new Random().nextInt(999999));
        verificationCodes.put(email, code);
        return code;
    }

    // Verify the code
    public boolean verifyCode(String email, String code) {
        return code.equals(verificationCodes.get(email));
    }

    // Optionally, clear the verification code after use
    public void clearVerificationCode(String email) {
        verificationCodes.remove(email);
    }
}