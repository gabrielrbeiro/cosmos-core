package com.inowhite.cosmos.core.service.impl;

import com.inowhite.cosmos.core.entity.SystemSetting;
import com.inowhite.cosmos.core.repository.SystemSettingRepository;
import com.inowhite.cosmos.core.service.SystemSettingService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DefaultSystemSettingService implements SystemSettingService {

  private final SystemSettingRepository systemSettingRepository;

  public DefaultSystemSettingService(SystemSettingRepository systemSettingRepository) {
    this.systemSettingRepository = systemSettingRepository;
  }

  @Override
  public boolean exists(String key) {
    return retrieve(key).isPresent();
  }

  @Override
  public Optional<SystemSetting> retrieve(String key) {
    return systemSettingRepository.findById(key);
  }

  @Override
  public void insertOrUpdate(String key, String value) {
    SystemSetting setting;
    var optional = retrieve(key);

    if (optional.isPresent()) {
      setting = optional.get();
    } else {
      setting = new SystemSetting()
        .setName(key)
        .setValue(value);
    }

    systemSettingRepository.save(setting);
  }

}
