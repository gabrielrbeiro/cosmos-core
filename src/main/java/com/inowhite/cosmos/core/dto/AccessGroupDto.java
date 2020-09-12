/*
 * Cosmos - IT Management and Service Desk System
 * Copyright (C) 2020  Gabriel Ribeiro
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
