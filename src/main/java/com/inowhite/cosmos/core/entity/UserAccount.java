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


import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "user_account")
public class UserAccount {

  @Id
  @GenericGenerator(
    name = "SnowflakeGenerator",
    strategy = "com.inowhite.cosmos.core.entity.generator.SnowflakeGenerator"
  )
  @GeneratedValue(generator = "SnowflakeGenerator")
  private Long id;

  private String firstName;
  private String lastName;
  private String username;
  private String emailAddress;
  private ZonedDateTime createdAt;
  private ZonedDateTime lastUpdate;
  private boolean enabled;

  @OneToOne(mappedBy = "user", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
  private UserCredential credential;

  @ManyToMany
  @JoinTable(
    name = "user_group_attachment",
    joinColumns = { @JoinColumn(name = "user_id") },
    inverseJoinColumns = { @JoinColumn(name = "group_id") }
  )
  private List<AccessGroup> groups;

  public UserAccount() {
    this.createdAt = ZonedDateTime.now();
    this.groups = new ArrayList<>();
    this.enabled = true;
  }

  public UserAccount setCredential(UserCredential credential) {
    this.credential = credential;
    this.credential.setUser(this);
    return this;
  }

  public UserAccount attachGroup(AccessGroup group) {
    this.groups.add(group);
    return this;
  }

  public UserAccount detachGroup(AccessGroup group) {
    this.groups.remove(group);
    return this;
  }

}
