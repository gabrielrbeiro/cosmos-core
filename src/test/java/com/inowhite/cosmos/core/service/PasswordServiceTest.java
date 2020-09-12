package com.inowhite.cosmos.core.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PasswordServiceTest {

  @Autowired
  public PasswordService passwordService;

  @Test
  public void testGenerateContext() {
    final int size = 128;
    var generated = passwordService.generateContext(size);
    assertNotNull(generated);
    assertEquals(size, generated.length);
  }

  @Test
  public void testEncodeWithContext() {
    var context = passwordService.generateContext(32);
    var encoded = passwordService.encodeWithContext(context, "plainPassword");
    assertNotNull(encoded);
  }

  @Test
  public void testVerifyPassword() {
    final var provided = "plainPassword";

    var context = passwordService.generateContext(32);
    var encoded = passwordService.encodeWithContext(context, provided);
    var result = passwordService.verifyWithContext(context, encoded, provided);
    assertTrue(result);
  }

}
