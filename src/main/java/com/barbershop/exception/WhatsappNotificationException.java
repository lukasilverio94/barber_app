package com.barbershop.exception;

public class WhatsappNotificationException extends RuntimeException{
    public WhatsappNotificationException(String message) {
        super(message);
    }

    public WhatsappNotificationException(String message, Throwable cause) {
        super(message, cause);
    }
}
