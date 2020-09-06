package com.inowhite.cosmos.core.controller;

import com.inowhite.cosmos.core.dto.ActionDto;
import com.inowhite.cosmos.core.dto.ErrorMessageDto;
import com.inowhite.cosmos.core.dto.mapper.ActionMapper;
import com.inowhite.cosmos.core.service.ActionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;

@RestController
@RequestMapping("/api/action")
public class ActionController {

  private final ActionService actionService;
  private final ActionMapper actionMapper;

  public ActionController(ActionService actionService, ActionMapper actionMapper) {
    this.actionService = actionService;
    this.actionMapper = actionMapper;
  }

  @GetMapping
  public ResponseEntity<?> listActions(@RequestParam(defaultValue = "1", required = false) int page,
                                       @RequestParam(defaultValue = "15", required = false) int size,
                                       @RequestParam(required = false) String query,
                                       @RequestParam(required = false) String orderSpecsStr) {

    var orderSpecs = new ArrayList<String>();
    if (orderSpecsStr != null && !orderSpecsStr.isBlank()) {
      var specs = orderSpecsStr.split(";");
      orderSpecs.addAll(Arrays.asList(specs));
    }
    var result = actionService.list(page, size, query, orderSpecs);
    var count = actionService.count(query);

    return ResponseEntity.status(HttpStatus.OK)
      .header("X-Count", String.valueOf(count))
      .body(result);

  }

  @PostMapping
  public ResponseEntity<?> createNewAction(@RequestBody ActionDto dto) {
    var action = actionMapper.mapToEntity(dto);
    var optional = actionService.findById(action.getCode());

    if (optional.isPresent()) {
      return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(new ErrorMessageDto("There's already an action with the same code"));
    }

    actionService.createOrUpdate(action);
    return ResponseEntity.status(HttpStatus.CREATED)
      .header("Location", String.format("/api/action/%s", action.getCode()))
      .body(dto);
  }

  @PatchMapping
  public ResponseEntity<?> updatePartially(@RequestBody ActionDto dto) {
    var optional = actionService.findById(dto.getCode());

    if (optional.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new ErrorMessageDto("The resource requested has not been found"));
    }

    var entity = optional.get();
    boolean updated = false;

    if (dto.getDescription() != null && !dto.getDescription().equals(entity.getDescription())) {
      entity.setDescription(dto.getDescription());
      updated = true;
    }

    if (!updated) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ErrorMessageDto("The resource has not been modified"));
    }

    actionService.createOrUpdate(entity);
    return ResponseEntity.status(HttpStatus.OK)
      .body(actionMapper.mapToDto(entity));

  }

  @DeleteMapping("{id}")
  public ResponseEntity<?> removeAction(@PathVariable String id) {
    var optional = actionService.findById(id);

    if (optional.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new ErrorMessageDto("The resource requested has not been found"));
    }

    var entity = optional.get();
    actionService.remove(entity);

    return ResponseEntity.status(HttpStatus.NO_CONTENT)
      .build();
  }

}
