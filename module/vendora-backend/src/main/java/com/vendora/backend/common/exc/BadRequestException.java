package com.vendora.backend.common.exc;

public class BadRequestException extends VendoraException {
  public BadRequestException(String message) {
    super(message);
  }
}
