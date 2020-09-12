/*
 * Cosmos - IT Management and Service Desk System
 * Copyright (C) 2020  Gabriel Ribeiro
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.inowhite.cosmos.core.service.impl;

import com.inowhite.cosmos.core.service.PasswordService;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;


@Service
public class DefaultPasswordService implements PasswordService {

  @Override
  public byte[] generateContext(int size) {
    byte[] bytes = new byte[size];
    new Random().nextBytes(bytes);
    return bytes;
  }

  @Override
  public String encodeWithContext(byte[] context, String provided) {
    var passwordBytes = provided.getBytes(StandardCharsets.UTF_8);
    var buffer = new byte[context.length + passwordBytes.length];

    System.arraycopy(context, 0, buffer, 0, context.length);
    System.arraycopy(passwordBytes, 0, buffer, context.length, passwordBytes.length);

    try {
      var md = MessageDigest.getInstance("SHA-256");
      md.update(buffer);
      var hashBytes = md.digest();
      return Base64.getEncoder().encodeToString(hashBytes);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public boolean verifyWithContext(byte[] context, String encoded, String provided) {
    if (encoded == null) {
      throw new IllegalArgumentException("The encoded parameter could not be null");
    }

    var providedEncoded = encodeWithContext(context, provided);
    return encoded.equals(providedEncoded);
  }
}
