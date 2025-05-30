package com.barbershop.exception;

public class BarberNotAvailableException extends RuntimeException {
  public BarberNotAvailableException(String message) {
    super(message);
  }
}
