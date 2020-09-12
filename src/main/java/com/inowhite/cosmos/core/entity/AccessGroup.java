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

package com.inowhite.cosmos.core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
@AllArgsConstructor

@Entity
@Table(name = "access_group")
public class AccessGroup {

  @Id
  @GenericGenerator(
    name = "SnowflakeGenerator",
    strategy = "com.inowhite.cosmos.core.entity.generator.SnowflakeGenerator"
  )
  @GeneratedValue(generator = "SnowflakeGenerator")
  private Long id;
  private String name;
  private String description;
  private ZonedDateTime createdAt;
  private ZonedDateTime lastUpdate;
  private boolean enabled;

  @ManyToMany
  @JoinTable(
    name = "group_grant",
    joinColumns = {@JoinColumn(name = "group_id")},
    inverseJoinColumns = {@JoinColumn(name = "action_code")}
  )
  private List<Action> grants;

  public AccessGroup() {
    this.createdAt = ZonedDateTime.now();
    this.enabled = true;
    this.grants = new ArrayList<>();
  }

}
