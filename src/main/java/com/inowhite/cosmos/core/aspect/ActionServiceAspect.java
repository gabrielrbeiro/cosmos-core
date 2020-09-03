package com.inowhite.cosmos.core.aspect;

import com.inowhite.cosmos.core.entity.Action;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ActionServiceAspect {

//  @Before("execution(* com.inowhite.cosmos.core.service.ActionService.list(..))")
//  public void verifyPageableParams(JoinPoint jPoint) {
//    AspectUtil.verifyPageableParams(jPoint);
//  }

  @Before("execution(* com.inowhite.cosmos.core.service.ActionService.remove(..))")
  public void detachActionFromGroupGrants(JoinPoint jPoint) {
    Action action = (Action) jPoint.getArgs()[0];

    if (action != null) {
      // TODO detach action from group grants before delete
    }
  }

}
