package com.inowhite.cosmos.core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.ZonedDateTime;
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
  private Boolean enabled;

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
  }

}
