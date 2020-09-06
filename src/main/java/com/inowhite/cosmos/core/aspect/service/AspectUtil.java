package com.inowhite.cosmos.core.aspect.service;

import org.aspectj.lang.JoinPoint;

public class AspectUtil {

  public static void verifyPageableParams(JoinPoint jPoint) {
    int page = (int) jPoint.getArgs()[0];
    if (page <= 0) {
      throw new IllegalArgumentException("Page index must be greater than or equals to 1");
    }

    int size = (int) jPoint.getArgs()[1];
    if (size <= 0) {
      throw new IllegalArgumentException("Page size must be greate than or equals to 1");
    }
  }

}
