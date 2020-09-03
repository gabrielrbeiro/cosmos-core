package com.inowhite.cosmos.core.service;

import com.inowhite.cosmos.core.entity.AccessGroup;
import com.inowhite.cosmos.core.repository.AccessGroupRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AccessGroupServiceTest {

  @Autowired
  public AccessGroupService accessGroupService;

  @Autowired
  public AccessGroupRepository accessGroupRepository;

  @Test
  public void testFailWhenUsingZeroOrNegativePageIndex() {
    assertThrows(IllegalArgumentException.class, () -> this.accessGroupService.list(0, 15));
  }

  @Test
  public void testFailWhenPageSizeIsLessThanOne() {
    assertThrows(IllegalArgumentException.class,
      () -> this.accessGroupService.list(10, 0));
  }

  @Test
  public void testShouldList() {
    insertBulkAccessGroups();
    var result = accessGroupService.list(1, 10);
    assertFalse(result.isEmpty());
  }

  @Test
  public void testShouldListWithQuery() {
    insertBulkAccessGroups();
    var result = accessGroupService.list(1, 10, "name==\"Test Group\"");
    assertFalse(result.isEmpty());
  }

  @Test
  public void testShouldListWithQueryWithOrdering() {
    insertBulkAccessGroups();
    var result = accessGroupService.list(1, 10, "name==\"Test Group\"", Arrays.asList("+id", "-createdAt"));
    assertFalse(result.isEmpty());
  }

  @Test
  public void testShouldThrowErrorWhenUsingIllegalArgs() {
    assertThrows(IllegalArgumentException.class,
      () -> accessGroupService.list(1, 10, null, Collections.singletonList("?id")));
  }

  @Test
  public void testShouldFindAccessGroupById() {
    var list = accessGroupService.list(1, 1);
    Long id;

    if (list.isEmpty()) {
      var group = new AccessGroup();
      group.setName("Access Group tests");
      accessGroupService.createOrUpdate(group);
      id = group.getId();
    } else {
      id = list.get(0).getId();
    }

    var result = accessGroupService.findById(id);
    assertTrue(result.isPresent());
  }

  @Test
  public void testShouldUpdateAccessGroup() {
    var list = accessGroupService.list(1, 1);
    AccessGroup group;

    if (list.isEmpty()) {
      group = new AccessGroup();
      group.setName("Access Group tests");
      accessGroupService.createOrUpdate(group);
    } else {
      group = list.get(0);
    }

    String beforeUpdate = String.format("%d", (group.getLastUpdate() != null) ?
      group.getLastUpdate().toEpochSecond(): 1L);
    group.setName("Changed access group name");
    accessGroupService.createOrUpdate(group);
    String afterUpdate = String.format("%d", group.getLastUpdate().toEpochSecond());
    assertNotEquals(beforeUpdate, afterUpdate);
  }

  @Test
  public void testShouldRemoveAccessGroupById() {
    if (accessGroupService.count(null) < 1L) {
      insertBulkAccessGroups();
    }

    var list = accessGroupService.list(1, 1);
    var result = accessGroupService.findById(list.get(0).getId());
    assertTrue(result.isPresent());
    accessGroupService.remove(result.get());
  }

  @Test
  public void testShouldCountWithQuery() {
    final String query = "name==\"Test Group\"";
    var count = accessGroupService.count(query);
    assertTrue(count >= 1L);
  }

  @Test
  public void testShouldSave() {
    var group = new AccessGroup();
    group.setName("New Test group");
    accessGroupService.createOrUpdate(group);
  }

  private void insertBulkAccessGroups() {
    IntStream.rangeClosed(0, 4)
      .mapToObj(m -> {
        var group = new AccessGroup();
        group.setCreatedAt(ZonedDateTime.now());
        group.setName("Test group");
        return group;
      })
      .collect(Collectors.toList())
      .forEach(action -> accessGroupRepository.save(action));
  }

}
