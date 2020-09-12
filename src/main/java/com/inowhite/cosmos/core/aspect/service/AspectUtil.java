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
