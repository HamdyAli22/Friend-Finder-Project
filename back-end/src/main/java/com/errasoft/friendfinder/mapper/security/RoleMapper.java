package com.errasoft.friendfinder.mapper.security;

import com.errasoft.friendfinder.dto.security.RoleDto;
import com.errasoft.friendfinder.model.security.Role;
import org.mapstruct.Mapper;


import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {


    Role toRole(RoleDto roleDto);

    //@Mapping(target = "account", ignore = true)
    RoleDto toRoleDto(Role role);

    List<Role> toRoleList (List<RoleDto> roleDto);

    List<RoleDto> toRoleDtoList (List<Role> roles);
}
