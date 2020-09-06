package com.inowhite.cosmos.core.aspect.service;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class BaseServiceAspect {

  @Before("execution(* com.inowhite.cosmos.core.service.BaseService.list(..))")
  public void verifyPageableParams(JoinPoint jPoint) {
    AspectUtil.verifyPageableParams(jPoint);
  }

}
