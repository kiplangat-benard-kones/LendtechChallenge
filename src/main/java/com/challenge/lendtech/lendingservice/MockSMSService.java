package com.challenge.lendtech.lendingservice;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class MockSMSService  implements SMSService{
    private static final Logger logger = (Logger) LoggerFactory.getLogger(MockSMSService.class);

    @Override
    public void sendSMS(String recipient, String message){
    logger.info("Sending SMS to "+ recipient + ":"+ message);
  }
}
