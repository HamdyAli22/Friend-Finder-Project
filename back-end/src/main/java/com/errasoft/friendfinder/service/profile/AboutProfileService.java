package com.errasoft.friendfinder.service.profile;

import com.errasoft.friendfinder.dto.profile.AboutProfileDto;

public interface AboutProfileService {

    AboutProfileDto createAboutProfile(AboutProfileDto dto);

    AboutProfileDto updateAboutProfile(AboutProfileDto dto);


    AboutProfileDto getAboutProfileByAccountId(Long accountId);

    void deleteAboutProfile(Long id);

    void deletePersonalInfo(Long id);

    void deleteAddress(Long id);

}
