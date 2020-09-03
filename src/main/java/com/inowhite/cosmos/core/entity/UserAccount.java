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
