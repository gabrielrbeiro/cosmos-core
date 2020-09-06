package com.inowhite.cosmos.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@Accessors(chain = true)
public class ErrorMessageDto {
  private String message;
}
