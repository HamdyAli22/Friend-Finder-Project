package com.errasoft.friendfinder.repo.profile;

import com.errasoft.friendfinder.model.profile.WorkExperience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkExperienceRepo extends JpaRepository<WorkExperience, Long> {
    Optional<WorkExperience> findByIdAndDeletedFalse(Long id);

    List<WorkExperience> findByAboutProfileAccountIdAndDeletedFalse(Long accountId);
}
