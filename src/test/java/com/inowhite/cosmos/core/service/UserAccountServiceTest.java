package com.inowhite.cosmos.core.service;

import com.inowhite.cosmos.core.entity.Action;
import com.inowhite.cosmos.core.entity.UserAccount;
import com.inowhite.cosmos.core.repository.UserAccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserAccountServiceTest {

  @Autowired
  public UserAccountService userAccountService;

  @Autowired
  public UserAccountRepository userAccountRepository;

  @Test
  public void testShouldListWithValidArguments() {
    insertBulkUserAccount();
    var result = userAccountService.list(1, 15);
    assertFalse(result.isEmpty());
  }

  @Test
  public void testShoudThrowAnExceptionWithInvalidArguments() {
    assertThrows(IllegalArgumentException.class,
      () -> userAccountService.list(0, 10));
    assertThrows(IllegalArgumentException.class,
      () -> userAccountService.list(1, 0));
  }

  @Test
  public void testShouldCount() {
    var count = userAccountService.count(null);
    assertNotNull(count);
  }

  @Test
  public void testShouldSave() {
    var userAccount = new UserAccount();
    userAccount.setFirstName("Mock");
    userAccount.setLastName("User");
    userAccount.setEmailAddress("mock.user@example.org");
    userAccount.setUsername("username");

    userAccountService.createOrUpdate(userAccount);
    assertNotNull(userAccount.getId());
    assertNotNull(userAccount.getCreatedAt());
    assertNull(userAccount.getLastUpdate());
    assertTrue(userAccount.isEnabled());
  }

  @Test
  public void testShouldFindById() {
    var results = userAccountService.list(1, 1);
    var singleResult = userAccountService.findById(results.get(0).getId());
    assertTrue(singleResult.isPresent());
  }

  @Test
  public void testShouldMakeASoftRemove() {
    var results = userAccountService.list(1, 1);
    var singleResult = userAccountService.findById(results.get(0).getId());
    assertTrue(singleResult.isPresent());

    var userAccount = singleResult.get();
    userAccountService.remove(userAccount);
    assertNotNull(userAccount.getLastUpdate());
    assertFalse(userAccount.isEnabled());
  }

  @Test
  public void testShouldCountWithQuery() {
    var result = userAccountService.count("firstName==Mock");
    assertNotNull(result);
  }

  @Test
  public void testShouldUpdateLastUpdateWhenSaving() {
    var results = userAccountService.list(1, 1);
    var userAccount = results.get(0);
    userAccountService.createOrUpdate(userAccount);
    assertNotNull(userAccount.getLastUpdate());
  }

  @Test
  public void testShouldListWithQueryArguments() {
    var results = userAccountService.list(1, 15, "firstName==Mock");
    assertFalse(results.isEmpty());
    assertTrue(results.size() <= 15);
  }

  @Test
  public void testShouldListWithQueryArgumentsAndSorting() {
    var ordering = Arrays.asList("+id", "-lastUpdate");
    var results = userAccountService.list(1, 15, "firstName==Mock", ordering);
    assertFalse(results.isEmpty());
    assertTrue(results.size() <= 15);
  }

  @Test
  public void testShouldThrowExceptionWithInvalidSorting() {
    var ordering = Collections.singletonList("?id");
    assertThrows(IllegalArgumentException.class,
      () -> userAccountService.list(1, 15, "firstName==Mock", ordering));
  }

  private void insertBulkUserAccount() {
    IntStream.rangeClosed(0, 4)
      .mapToObj(m -> {
        var userAccount = new UserAccount();
        userAccount.setFirstName("Mock");
        userAccount.setLastName("User");
        userAccount.setEmailAddress("mock.user@example.org");
        userAccount.setUsername("username");

        return userAccount;
      })
      .collect(Collectors.toList())
      .forEach(action -> userAccountRepository.save(action));
  }

}
