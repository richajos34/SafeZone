
/**
 * package com.berkeley.irms.warnme.services;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    @Value("4845e08f42d2d5e0213bd070d5ae2f7d")
    private String accountSid;

    @Value("845e08f42d2d5e0213bd070d5ae2f7d")
    private String authToken;

    @Value("+18773756339")
    private String twilioPhoneNumber;

    public SmsService() {
        Twilio.init(accountSid, authToken);
    }

    public void sendSms(String to, String message) {
        Message.creator(
                new PhoneNumber(to),
                new PhoneNumber(twilioPhoneNumber),
                message
        ).create();
    }
}
**/
