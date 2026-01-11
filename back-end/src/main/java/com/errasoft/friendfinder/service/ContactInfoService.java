package com.errasoft.friendfinder.service;

import com.errasoft.friendfinder.controller.vm.ContactInfoResponseVm;
import com.errasoft.friendfinder.dto.ContactInfoDto;

public interface ContactInfoService {
    ContactInfoDto createContactInfo(ContactInfoDto contactInfoDto);
    ContactInfoResponseVm getAllContacts(int page, int size);
    ContactInfoResponseVm getContactsByEmail(String email,int page, int size);
    ContactInfoDto updateMessage(ContactInfoDto contactInfoDto);
    ContactInfoResponseVm searchContacts(String keyword, int page, int size);
}
