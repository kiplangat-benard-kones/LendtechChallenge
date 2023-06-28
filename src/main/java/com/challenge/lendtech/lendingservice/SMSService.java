package com.challenge.lendtech.lendingservice;


import org.springframework.stereotype.Service;

@Service
public interface SMSService {
    void sendSMS(String recipient, String message);
}
