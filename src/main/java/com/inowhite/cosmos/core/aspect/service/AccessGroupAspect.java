package com.inowhite.cosmos.core.aspect.service;

import com.inowhite.cosmos.core.entity.AccessGroup;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Aspect
@Component
public class AccessGroupAspect {

  @Before("execution (* com.inowhite.cosmos.core.service.AccessGroupService.createOrUpdate(..))")
  public void prepareAccessGroupBeforeSaving(JoinPoint jPoint) {
    if (jPoint.getArgs().length > 0 && jPoint.getArgs()[0] instanceof AccessGroup) {
      var group = (AccessGroup) jPoint.getArgs()[0];

      if (group.getId() == null) {
        group.setCreatedAt(ZonedDateTime.now());
        group.setLastUpdate(null);
        group.setEnabled(true);
      } else {
        group.setLastUpdate(ZonedDateTime.now());
      }
    }
  }

}
