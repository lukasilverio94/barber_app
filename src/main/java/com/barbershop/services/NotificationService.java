package com.barbershop.services;

import com.barbershop.config.TwilioConfig;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationService {

    private final TwilioConfig twilioConfig;

    public NotificationService(TwilioConfig twilioConfig) {
        this.twilioConfig = twilioConfig;
    }

    @PostConstruct
    public void init() {
        Twilio.init(twilioConfig.getAccountSid(), twilioConfig.getAuthToken());
        log.info("Twilio initialized for account: {}", twilioConfig.getAccountSid());
    }

    public void sendWhatsAppMessage(String to, String message) {
        try {
            String formattedTo = formatPhoneNumber(to);
            String formattedFrom = formatPhoneNumber(twilioConfig.getPhoneNumber());

            Message.creator(
                    new PhoneNumber("whatsapp:" + formattedTo),
                    new PhoneNumber("whatsapp:" + formattedFrom),
                    message
            ).create();

            log.info("WhatsApp message sent to {}", formattedTo);
        } catch (Exception e) {
            log.error("Failed to send WhatsApp message to {}", to, e);
            throw new RuntimeException("Failed to send WhatsApp message", e);
        }
    }

    private String formatPhoneNumber(String number) {
        // Remove all non-digit characters except +
        String cleaned = number.replaceAll("[^0-9+]", "");

        // Ensure proper + prefix
        if (!cleaned.startsWith("+")) {
            cleaned = "+" + cleaned;
        }

        return cleaned;
    }
}