package com.errasoft.friendfinder.service.impl.security;

import com.errasoft.friendfinder.dto.security.RoleDto;
import com.errasoft.friendfinder.mapper.security.RoleMapper;
import com.errasoft.friendfinder.repo.security.RoleRepo;
import com.errasoft.friendfinder.service.security.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    private RoleRepo roleRepo;
    private RoleMapper roleMapper;

    public RoleServiceImpl(RoleRepo roleRepo, RoleMapper roleMapper) {
        this.roleRepo = roleRepo;
        this.roleMapper = roleMapper;
    }

    @Override
    public RoleDto createRole(RoleDto roleDto) {
        return null;
    }
}
