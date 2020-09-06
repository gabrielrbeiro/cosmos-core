package com.inowhite.cosmos.core.dto.mapper;

import com.inowhite.cosmos.core.dto.AccessGroupDto;
import com.inowhite.cosmos.core.entity.AccessGroup;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccessGroupMapper {

  @InheritInverseConfiguration
  AccessGroupDto mapToDto(AccessGroup group);

  @Mapping(target = "enabled", ignore = true)
  AccessGroup mapToEntity(AccessGroupDto group);

}
