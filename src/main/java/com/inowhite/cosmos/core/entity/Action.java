package com.inowhite.cosmos.core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "action")
public class Action {

  @Id
  private String code;
  private String description;

}
