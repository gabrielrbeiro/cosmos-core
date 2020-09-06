package com.inowhite.cosmos.core.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ActionDto {
  private String code;
  private String description;
}
