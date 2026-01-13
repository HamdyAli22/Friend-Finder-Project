package com.errasoft.friendfinder.repo.profile;

import com.errasoft.friendfinder.model.profile.Interest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InterestRepo extends JpaRepository<Interest, Long> {
    Optional<Interest> findByIdAndDeletedFalse(Long id);

    List<Interest> findByAboutProfileAccountIdAndDeletedFalse(Long accountId);
}
