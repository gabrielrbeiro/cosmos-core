package com.inowhite.cosmos.core.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
public class AccessGroupDto {
  private Long id;
  private String name;
  private String description;
  private List<ActionDto> grants;
  private ZonedDateTime createdAt;
  private ZonedDateTime lastUpdate;
}
