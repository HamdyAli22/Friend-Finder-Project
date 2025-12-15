package com.errasoft.friendfinder.repo.security;

import com.errasoft.friendfinder.model.security.Account;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepo extends JpaRepository<Account,Long> {

    @EntityGraph(attributePaths = "roles")
    Optional<Account> findByEmail(String email);

    Optional<Account> findFirstByRoles_RoleNameIgnoreCase(String roleName);

    List<Account> findByIdInAndEnabledTrue(List<Long> ids);


}
