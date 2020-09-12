package com.inowhite.cosmos.core.service;

import com.inowhite.cosmos.core.entity.AccessGroup;
import com.inowhite.cosmos.core.repository.AccessGroupRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class StartupServiceTest {

  @Autowired
  public StartupService startupService;

  @Autowired
  public AccessGroupRepository accessGroupRepository;

  @Test
  public void testCreateAdminGroup() {
    var group = startupService.createAdminGroup();
    assertNotNull(group);
    assertNotNull(group.getId());
    assertNotNull(group.getCreatedAt());
    assertTrue(group.isEnabled());
  }

  @Test
  public void testCreateAdministratorAccountWithInvalidArgs() {
    assertThrows(IllegalArgumentException.class,
      () -> startupService.createAdministratorAccount("admin", "admin", null));
  }

  @Test
  public void testCreateAdministratorAccountWithValidArgs() {
    var results = accessGroupRepository.findAll();
    var result = startupService.createAdministratorAccount("admin", "admin", results.get(0));
    assertNotNull(result);
    assertNotNull(result.getId());
    assertNotNull(result.getCreatedAt());
    assertTrue(result.isEnabled());
  }

}
