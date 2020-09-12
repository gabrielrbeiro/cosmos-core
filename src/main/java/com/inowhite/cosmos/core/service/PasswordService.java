package com.inowhite.cosmos.core.service;

public interface PasswordService {
  byte[] generateContext(int size);
  String encodeWithContext(byte[] context, String provided);
  boolean verifyWithContext(byte[] context, String encoded, String provided);
}
