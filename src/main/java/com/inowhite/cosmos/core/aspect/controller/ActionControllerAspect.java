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

package com.inowhite.cosmos.core.aspect.controller;

import com.inowhite.cosmos.core.dto.ActionDto;
import com.inowhite.cosmos.core.dto.ErrorMessageDto;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.HashMap;

@Aspect
@Component
public class ActionControllerAspect {

  public static final String CHANNEL_CREATED = "core.action-created";
  public static final String CHANNEL_UPDATED = "core.action-updated";
  public static final String CHANNEL_DELETED = "core.action-deleted";

  private static final Logger logger = LoggerFactory.getLogger(ActionControllerAspect.class);

  private final KafkaTemplate<String, Object> kafkaTemplate;

  public ActionControllerAspect(KafkaTemplate<String, Object> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  @Around("execution(* com.inowhite.cosmos.core.controller.ActionController.listActions(..))")
  public ResponseEntity<?> handleList(ProceedingJoinPoint jPoint) {

    try {

      var result = jPoint.proceed();

      if (result instanceof ResponseEntity) {
        return (ResponseEntity<?>) result;
      }

      throw new IllegalArgumentException("Invalid response");

    } catch (IllegalArgumentException ex) {

      logger.error("An exception has been raised", ex);

      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ErrorMessageDto(ex.getMessage()));

    } catch (Throwable t) {
      return handleException(t);
    }

  }

  @Around("execution(* com.inowhite.cosmos.core.controller.ActionController.createNewAction(..))")
  public ResponseEntity<?> handleCreation(ProceedingJoinPoint jPoint) {

    try {

      var result = jPoint.proceed();

      if (result instanceof ResponseEntity) {

        var entity = (((ResponseEntity<ActionDto>) result).getStatusCode() != HttpStatus.CREATED) ? null :
          (ResponseEntity<ActionDto>) result;

        if (entity != null) {
          Thread thread = new Thread(() -> {
            kafkaTemplate.send(CHANNEL_CREATED, entity.getBody());
          });

          thread.start();
        }

        return (ResponseEntity) result;

      }

      throw new IllegalArgumentException("Invalid response");

    } catch (IllegalArgumentException ex) {

      logger.error("An exception has been raised", ex);

      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ErrorMessageDto(ex.getMessage()));

    } catch (Throwable t) {
      return handleException(t);
    }

  }

  @Around("execution(* com.inowhite.cosmos.core.controller.ActionController.updatePartially(..))")
  public ResponseEntity<?> handleUpdate(ProceedingJoinPoint jPoint) {

    var dto = (ActionDto) jPoint.getArgs()[0];

    if (dto.getDescription() == null || dto.getCode() == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ErrorMessageDto("You should send code and description in request"));
    }

    try {

      var result = (ResponseEntity) jPoint.proceed();

      if (result.getStatusCode() == HttpStatus.OK) {
        Thread thread = new Thread(() -> {
          kafkaTemplate.send(CHANNEL_UPDATED, result.getBody());
        });

        thread.start();
      }

      return result;

    } catch(Throwable t) {
      return handleException(t);
    }

  }

  @Around("execution(* com.inowhite.cosmos.core.controller.ActionController.removeAction(..))")
  public ResponseEntity<?> handleDeletion(ProceedingJoinPoint jPoint) {
    var code = (String) jPoint.getArgs()[0];

    try {

      var result = (ResponseEntity) jPoint.proceed();

      if (result.getStatusCode() == HttpStatus.NO_CONTENT) {
        Thread thread = new Thread(() -> kafkaTemplate.send(CHANNEL_DELETED, new HashMap<String, String>(){{
          put("action_code", code);
        }}));
        thread.start();
      }

      return result;

    } catch(Throwable t) {
      return handleException(t);
    }
  }

  private ResponseEntity<ErrorMessageDto> handleException(Throwable t) {
    logger.error("An exception has been raised", t);
    final String messageFormat = "Internal Server Error, due to error: %s";

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body(new ErrorMessageDto(String.format(messageFormat, t.getMessage())));
  }

}
