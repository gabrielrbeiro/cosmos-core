package com.inowhite.cosmos.core.aspect.service;

import com.inowhite.cosmos.core.entity.UserAccount;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;

import java.time.ZonedDateTime;

@Aspect
@Configuration
public class UserAccountAspect {

  @Before("execution(* com.inowhite.cosmos.core.service.UserAccountService.createOrUpdate(..))")
  public void beforeSaveOrUpdate(JoinPoint jPoint) {
    var userAccount = (UserAccount) jPoint.getArgs()[0];

    if (userAccount.getId() == null) {
      userAccount.setCreatedAt(ZonedDateTime.now());
      userAccount.setLastUpdate(null);
      userAccount.setEnabled(true);
    } else {
      userAccount.setLastUpdate(ZonedDateTime.now());
    }
  }

}
