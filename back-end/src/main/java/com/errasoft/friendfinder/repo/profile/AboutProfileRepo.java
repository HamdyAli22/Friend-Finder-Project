package com.errasoft.friendfinder.repo.profile;

import com.errasoft.friendfinder.model.profile.AboutProfile;
import com.errasoft.friendfinder.model.security.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AboutProfileRepo extends JpaRepository<AboutProfile, Long> {

    Optional<AboutProfile> findByIdAndDeletedFalse(Long id);

    Optional<AboutProfile> findByAccountIdAndDeletedFalse(Long accountId);

    boolean existsByAccountAndDeletedFalse(Account account);


}
