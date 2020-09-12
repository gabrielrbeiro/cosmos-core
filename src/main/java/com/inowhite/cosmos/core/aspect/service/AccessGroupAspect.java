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
