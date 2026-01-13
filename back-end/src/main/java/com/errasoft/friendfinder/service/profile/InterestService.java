package com.errasoft.friendfinder.service.profile;

import com.errasoft.friendfinder.dto.profile.InterestDto;

import java.util.List;

public interface InterestService {
    InterestDto createInterest(InterestDto interestDto);

    InterestDto updateInterest(InterestDto interestDto);

    List<InterestDto> getInterestsByAccountId(Long accountId);

    void deleteInterest(Long id);
}
