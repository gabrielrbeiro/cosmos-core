package com.inowhite.cosmos.core.service;

import com.inowhite.cosmos.core.entity.SystemSetting;

import java.util.Optional;

public interface SystemSettingService {
  boolean exists(String key);
  Optional<SystemSetting> retrieve(String key);
  void insertOrUpdate(String key, String value);
}
