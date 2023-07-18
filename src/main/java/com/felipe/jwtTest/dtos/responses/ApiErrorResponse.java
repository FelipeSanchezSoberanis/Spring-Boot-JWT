package com.felipe.jwtTest.dtos.responses;

import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ApiErrorResponse {
  private String message;
  private ZonedDateTime timestamp = ZonedDateTime.now();
}
