package com.errasoft.friendfinder.repo.profile;


import com.errasoft.friendfinder.model.profile.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LanguageRepo  extends JpaRepository<Language, Long> {
    Optional<Language> findByIdAndDeletedFalse(Long id);

    List<Language> findByAboutProfileAccountIdAndDeletedFalse(Long accountId);
}
