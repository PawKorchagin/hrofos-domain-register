package ru.itmo.auth.exception;

public class InvalidTOTPException extends RuntimeException {
  public InvalidTOTPException(String message) {
      super(message);
  }
}
