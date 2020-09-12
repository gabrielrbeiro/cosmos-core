package com.inowhite.cosmos.core.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Accessors(chain = true)

@Entity
@Table(name = "system_setting")
public class SystemSetting {

  @Id
  private String name;
  private String value;

}
