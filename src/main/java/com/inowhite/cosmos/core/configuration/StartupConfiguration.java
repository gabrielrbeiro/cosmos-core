package com.inowhite.cosmos.core.configuration;

import com.inowhite.cosmos.core.service.AccessGroupService;
import com.inowhite.cosmos.core.service.ActionService;
import com.inowhite.cosmos.core.service.UserAccountService;
import lombok.Data;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Data
@Configuration
public class StartupConfiguration {

  private final UserAccountService userAccountService;
  private final AccessGroupService accessGroupService;
  private final ActionService actionService;

  public StartupConfiguration(UserAccountService userAccountService,
                              AccessGroupService accessGroupService,
                              ActionService actionService) {
    this.userAccountService = userAccountService;
    this.accessGroupService = accessGroupService;
    this.actionService = actionService;
  }

  @PostConstruct
  public void makeInitialConfiguration() {



  }

}
