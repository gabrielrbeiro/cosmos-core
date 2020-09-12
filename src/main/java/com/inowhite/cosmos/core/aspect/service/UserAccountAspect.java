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
