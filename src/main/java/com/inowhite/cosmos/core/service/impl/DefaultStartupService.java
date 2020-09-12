package com.inowhite.cosmos.core.service.impl;

import com.inowhite.cosmos.core.entity.AccessGroup;
import com.inowhite.cosmos.core.entity.UserAccount;
import com.inowhite.cosmos.core.entity.UserCredential;
import com.inowhite.cosmos.core.repository.AccessGroupRepository;
import com.inowhite.cosmos.core.repository.ActionRepository;
import com.inowhite.cosmos.core.service.PasswordService;
import com.inowhite.cosmos.core.service.StartupService;
import com.inowhite.cosmos.core.service.UserAccountService;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class DefaultStartupService implements StartupService {

  private final static String ACT_ADMIN_ACCESS = "cosmosCore:admin";

  private final ActionRepository actionRepository;
  private final AccessGroupRepository accessGroupRepository;
  private final UserAccountService userAccountService;
  private final PasswordService passwordService;

  public DefaultStartupService(ActionRepository actionRepository,
                               AccessGroupRepository accessGroupRepository,
                               UserAccountService userAccountService,
                               PasswordService passwordService) {
    this.actionRepository = actionRepository;
    this.accessGroupRepository = accessGroupRepository;
    this.userAccountService = userAccountService;
    this.passwordService = passwordService;
  }

  @Override
  public AccessGroup createAdminGroup() {
    var action = actionRepository.findById(ACT_ADMIN_ACCESS);
    if (action.isEmpty()) {
      throw new IllegalStateException("Could not find 'cosmosCore:admin' action, check installation.");
    }

    var group = new AccessGroup()
      .setDescription("Full access to all managed resources")
      .setName("Administrators");

    group.getGrants().add(action.get());

    accessGroupRepository.save(group);
    return group;
  }

  @Override
  public UserAccount createAdministratorAccount(String username, String password, AccessGroup group) {
    if (group == null) {
      throw new IllegalArgumentException("You should provide a group to this user");
    }

    var userAccount = new UserAccount()
      .setFirstName("Administrator")
      .setUsername(username)
      .setEmailAddress("admin@localhost");

    userAccount.getGroups().add(group);

    var creds = new UserCredential()
      .setUser(userAccount);

    var context = passwordService.generateContext(128);
    var encoded = passwordService.encodeWithContext(context, password);
    creds.setContext(Base64.getEncoder().encodeToString(context))
      .setHash(encoded);

    userAccount.setCredential(creds);
    userAccountService.createOrUpdate(userAccount);

    return userAccount;
  }

}
