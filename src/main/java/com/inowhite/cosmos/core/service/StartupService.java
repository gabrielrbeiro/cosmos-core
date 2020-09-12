package com.inowhite.cosmos.core.service;

import com.inowhite.cosmos.core.entity.AccessGroup;
import com.inowhite.cosmos.core.entity.UserAccount;

public interface StartupService {
  AccessGroup createAdminGroup();
  UserAccount createAdministratorAccount(String username, String password, AccessGroup adminGroup);
}
