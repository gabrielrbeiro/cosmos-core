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
