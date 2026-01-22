package com.errasoft.friendfinder.service.impl.profile;

import com.errasoft.friendfinder.dto.profile.InterestDto;
import com.errasoft.friendfinder.mapper.profile.InterestMapper;
import com.errasoft.friendfinder.model.profile.AboutProfile;
import com.errasoft.friendfinder.model.profile.Interest;
import com.errasoft.friendfinder.repo.profile.AboutProfileRepo;
import com.errasoft.friendfinder.repo.profile.InterestRepo;
import com.errasoft.friendfinder.service.profile.InterestService;
import com.errasoft.friendfinder.service.security.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;

@Service
public class InterestServiceImpl implements InterestService {

    private InterestRepo interestRepo;
    private AboutProfileRepo aboutProfileRepo;
    private InterestMapper interestMapper;
    private AuthService authService;

    @Autowired
    public InterestServiceImpl(InterestRepo interestRepo,
                               AboutProfileRepo aboutProfileRepo,
                               InterestMapper interestMapper,
                               AuthService authService) {
        this.interestRepo = interestRepo;
        this.aboutProfileRepo = aboutProfileRepo;
        this.interestMapper = interestMapper;
        this.authService = authService;
    }

    @Override
    public InterestDto createInterest(InterestDto interestDto) {

        if (Objects.nonNull(interestDto.getId())) {
            throw new RuntimeException("id.must_be.null");
        }

        Long accountId = authService.getCurrentUserId();

        AboutProfile aboutProfile = aboutProfileRepo
                .findByAccountIdAndDeletedFalse(accountId)
                .orElseThrow(() -> new RuntimeException("about.profile.not.found"));

        Interest interest = interestMapper.toInterest(interestDto);
        interest.setAboutProfile(aboutProfile);
        interest.setDeleted(false);

        Interest saved = interestRepo.save(interest);
        return interestMapper.toInterestDto(saved);
    }

    @Override
    public InterestDto updateInterest(InterestDto interestDto) {

        if (interestDto.getId() == null) {
            throw new RuntimeException("id.must_be.not_null");
        }

        Interest existing = interestRepo
                .findByIdAndDeletedFalse(interestDto.getId())
                .orElseThrow(() -> new RuntimeException("interest.not.found"));

        if (interestDto.getName() != null) {
            existing.setName(interestDto.getName());
        }

        Interest updated = interestRepo.save(existing);
        return interestMapper.toInterestDto(updated);
    }

    @Override
    public List<InterestDto> getInterestsByAccountId(Long accountId) {
        List<Interest> interests = interestRepo
                .findByAboutProfileAccountIdAndDeletedFalse(accountId);

        if (interests.isEmpty()) {
            throw new RuntimeException("interest.not.found");
        }

        return interests.stream()
                .map(interestMapper::toInterestDto)
                .toList();
    }

    @Override
    public void deleteInterest(Long id) {

        Long accountId = authService.getCurrentUserId();

        if (id != null) {

            Interest existing = interestRepo
                    .findByIdAndDeletedFalse(id)
                    .orElseThrow(() -> new RuntimeException("interest.not.found"));


            if (!existing.getAboutProfile().getAccount().getId().equals(accountId)) {
                throw new RuntimeException("account.user.denied");
            }

            existing.setDeleted(true);
            interestRepo.save(existing);

        } else {
            List<Interest> interests =
                    interestRepo.findByAboutProfileAccountIdAndDeletedFalse(accountId);

            if (interests.isEmpty()) {
                return;
            }

            interests.forEach(i -> i.setDeleted(true));
            interestRepo.saveAll(interests);
        }
    }
}
