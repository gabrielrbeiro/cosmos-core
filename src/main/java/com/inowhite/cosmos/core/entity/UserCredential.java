package com.inowhite.cosmos.core.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "user_credential")
public class UserCredential {

  @Id
  private Long id;

  @OneToOne
  @MapsId
  private UserAccount user;

  private String context;
  private String hash;

}
