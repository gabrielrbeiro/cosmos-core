package com.inowhite.cosmos.core.configuration;

import com.inowhite.cosmos.core.service.*;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Configuration
public class StartupConfiguration {

  private static final Logger logger = LoggerFactory.getLogger(StartupConfiguration.class);
  private static final String CFG_SETUP_DATE = "cosmos:core:global/setup-date";

  private final UserAccountService userAccountService;
  private final AccessGroupService accessGroupService;
  private final ActionService actionService;
  private final SystemSettingService systemSettingService;
  private final StartupService startupService;

  public StartupConfiguration(UserAccountService userAccountService,
                              AccessGroupService accessGroupService,
                              ActionService actionService,
                              StartupService startupService,
                              SystemSettingService systemSettingService) {
    this.userAccountService = userAccountService;
    this.accessGroupService = accessGroupService;
    this.actionService = actionService;
    this.startupService = startupService;
    this.systemSettingService = systemSettingService;
  }

  @PostConstruct
  public void makeInitialConfiguration() {
    var exists = systemSettingService.exists(CFG_SETUP_DATE);
    var username = "admin";
    var password = "admin";

    if (!exists) {
      var group = startupService.createAdminGroup();
      var user = startupService.createAdministratorAccount(username, password, group);
      logger.info("Admin user with username '{}' and password '{}', please change on first login", username, password);

      var now = ZonedDateTime.now();
      var dtf = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
      systemSettingService.insertOrUpdate(CFG_SETUP_DATE, dtf.format(now));
    }
  }

}
