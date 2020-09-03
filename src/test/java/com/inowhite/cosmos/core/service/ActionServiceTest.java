package com.inowhite.cosmos.core.service;

import com.inowhite.cosmos.core.entity.Action;
import com.inowhite.cosmos.core.repository.ActionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ActionServiceTest {

  @Autowired
  public ActionService actionService;

  @Autowired
  public ActionRepository actionRepository;

  @Test
  public void testFailWhenUsingZeroOrNegativePageIndex() {
    assertThrows(IllegalArgumentException.class,
      () -> this.actionService.list(0, 15));
  }

  @Test
  public void testFailWhenPageSizeIsLessThanOne() {
    assertThrows(IllegalArgumentException.class,
      () -> this.actionService.list(10, 0));
  }

  @Test
  public void testShouldList() {
    insertBulkActions();
    var result = actionService.list(1, 10);
    assertFalse(result.isEmpty());
  }

  @Test
  public void testShouldListWithQuery() {
    insertBulkActions();
    var result = actionService.list(1, 10, "code==action:1");
    assertFalse(result.isEmpty());
  }

  @Test
  public void testShouldListWithQueryWithOrdering() {
    insertBulkActions();
    var result = actionService.list(1, 10, "code==action:1", Arrays.asList("+code", "-description"));
    assertFalse(result.isEmpty());
  }

  @Test
  public void testShouldThrowErrorWhenUsingIllegalArgs() {
    assertThrows(IllegalArgumentException.class,
      () -> actionService.list(1, 10, null, Collections.singletonList("?code")));
  }

  @Test
  public void testShouldFindActionById() {
    final String id = "action:1";
    var result = actionService.findById(id);
    assertTrue(result.isPresent());
  }

  @Test
  public void testShouldRemoveActionById() {
    if (actionService.count(null) < 1L) {
      insertBulkActions();
    }

    final String id = "action:2";
    var result = actionService.findById(id);
    actionService.remove(result.get());
  }

  @Test
  public void testShouldCountWithQuery() {
    final String query = "code==action:1";
    var count = actionService.count(query);
    assertEquals(1L, count);
  }

  @Test
  public void testShouldSave() {
    var action = new Action("action:10", "action ten");
    actionService.createOrUpdate(action);
  }

  private void insertBulkActions() {
    IntStream.rangeClosed(0, 4)
      .mapToObj(m -> new Action(
        String.format("action:%d", m),
        String.format("Action n. %d", m)
      ))
      .collect(Collectors.toList())
      .forEach(action -> actionRepository.save(action));
  }

}
