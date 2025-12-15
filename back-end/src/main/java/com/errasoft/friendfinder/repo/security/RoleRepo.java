package com.errasoft.friendfinder.repo.security;

import com.errasoft.friendfinder.model.security.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepo extends JpaRepository<Role,Long> {

    Optional<Role> findByRoleName(String roleName);

}
