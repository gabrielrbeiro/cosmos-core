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

package com.inowhite.cosmos.core.controller;

import com.inowhite.cosmos.core.dto.AccessGroupDto;
import com.inowhite.cosmos.core.dto.ErrorMessageDto;
import com.inowhite.cosmos.core.dto.mapper.AccessGroupMapper;
import com.inowhite.cosmos.core.service.AccessGroupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/access_group")
public class AccessGroupController {

  private final AccessGroupService accessGroupService;
  private final AccessGroupMapper accessGroupMapper;

  public AccessGroupController(AccessGroupService accessGroupService, AccessGroupMapper accessGroupMapper) {
    this.accessGroupService = accessGroupService;
    this.accessGroupMapper = accessGroupMapper;
  }

  @GetMapping
  public ResponseEntity<?> listAll(@RequestParam(defaultValue = "1", required = false) int page,
                                   @RequestParam(defaultValue = "15", required = false) int size,
                                   @RequestParam(required = false) String query,
                                   @RequestParam(required = false) String orderSpecsStr) {

    var orderSpecs = ControllerUtil.getOrderSpecAsList(orderSpecsStr);
    var result = accessGroupService.list(page, size, query, orderSpecs);
    var count = accessGroupService.count(query);

    return ResponseEntity.status(HttpStatus.OK)
      .header("X-Count", String.valueOf(count))
      .body(result);

  }

  @PostMapping
  public ResponseEntity<?> create(@RequestBody AccessGroupDto dto) {
    var group = accessGroupMapper.mapToEntity(dto);
    accessGroupService.createOrUpdate(group);

    dto.setId(group.getId());
    return ResponseEntity.status(HttpStatus.OK)
      .body(dto);
  }

  @PutMapping
  public ResponseEntity<?> update(@RequestBody AccessGroupDto dto) {
    var optional = accessGroupService.findById(dto.getId());
    if (optional.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new ErrorMessageDto("Requested access group could not be found"));
    }

    var group = accessGroupMapper.mapToEntity(dto);
    group.setCreatedAt(optional.get().getCreatedAt());

    accessGroupService.createOrUpdate(group);
    return ResponseEntity.ok(accessGroupMapper.mapToDto(group));
  }

  @DeleteMapping("{id}")
  public ResponseEntity<?> delete(@PathVariable Long id) {
    var optional = accessGroupService.findById(id);
    if (optional.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new ErrorMessageDto("Requested access group could not be found"));
    }

    accessGroupService.remove(optional.get());
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

}
