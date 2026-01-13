package com.vendora.backend.common.web.api;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class ApiError {
  private String type;
  private String message;
  private Timestamp timestamp;

  public ApiError(RuntimeException exc) {
    this.type = exc.getClass().getSimpleName();
    this.message = exc.getMessage();
    this.timestamp = new Timestamp(System.currentTimeMillis());
  }
}

