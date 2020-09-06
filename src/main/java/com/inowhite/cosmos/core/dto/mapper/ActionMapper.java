package com.inowhite.cosmos.core.dto.mapper;

import com.inowhite.cosmos.core.dto.ActionDto;
import com.inowhite.cosmos.core.entity.Action;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ActionMapper {
  ActionDto mapToDto(Action action);
  Action mapToEntity(ActionDto dto);
}
