package com.barbershop.services;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final String ACCOUNT_SID = "";
    private final String AUTH_TOKEN = "";
    private final String FROM_NUMBER = "886";

    public NotificationService() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    public void sendWhatsAppMessage(String to, String message) {
        Message.creator(
                new PhoneNumber("whatsapp:" + to),
                new PhoneNumber(FROM_NUMBER),
                message
        ).create();
    }
}
