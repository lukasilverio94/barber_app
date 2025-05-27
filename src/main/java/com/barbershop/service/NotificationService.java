package com.barbershop.service;

import com.barbershop.config.TwilioConfig;
import com.barbershop.model.Appointment;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class NotificationService {
    private static final DateTimeFormatter BRAZIL_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
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

    public void notifyNewAppointmentRequestToBarber(Appointment appointment) {
        String message = String.format(
                "✂️ Novo pedido de agendamento!\n📅 Data: %s\n🕒 Hora: %s\n✂️ Serviço: %s\n👤 Cliente: %s",
                appointment.getApptDay().format(BRAZIL_DATE_FORMATTER),
                appointment.getStartTime(),
                appointment.getServiceType().getPortugueseDescription(),
                appointment.getCustomer().getName()
        );
        sendWhatsAppMessage(appointment.getBarber().getPhone(), message);
    }

    public void notifyNewAppointmentRequestToCustomer(Appointment appointment) {
        String message = String.format(
                "✂️ Seu pedido de agendamento foi recebido!\n📅 Data: %s\n🕒 Hora: %s\n✂️ Serviço: %s\n💈 Barbeiro: %s",
                appointment.getApptDay().format(BRAZIL_DATE_FORMATTER),
                appointment.getStartTime(),
                appointment.getServiceType().getPortugueseDescription(),
                appointment.getBarber().getName()
        );
        sendWhatsAppMessage(appointment.getCustomer().getPhone(), message);
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


    public void notifyAppointmentAccepted(Appointment appointment) {
        String message = String.format(
                "✅ Agendamento Confirmado!\n📅 Data: %s\n🕒 Hora: %s\n✂️ Serviço: %s\n👤 Barbeiro: %s",
                appointment.getApptDay().format(BRAZIL_DATE_FORMATTER),
                appointment.getStartTime(),
                appointment.getServiceType().getPortugueseDescription(),
                appointment.getBarber().getName()
        );
        sendWhatsAppMessage(appointment.getCustomer().getPhone(), message);
    }

    public void notifyAppointmentCanceled(Appointment appointment) {
        String message = String.format(
                "❌ Seu agendamento em %s às %s foi cancelado.",
                appointment.getApptDay().format(BRAZIL_DATE_FORMATTER),
                appointment.getStartTime()
        );
        sendWhatsAppMessage(appointment.getCustomer().getPhone(), message);
    }
}