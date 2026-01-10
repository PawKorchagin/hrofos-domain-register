package ru.itmo.auth.exception;

public class TOTPRequiredException extends RuntimeException {
  public TOTPRequiredException(String message) {
      super(message);
  }
}
