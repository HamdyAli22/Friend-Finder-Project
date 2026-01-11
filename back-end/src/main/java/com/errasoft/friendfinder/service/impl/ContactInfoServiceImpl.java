package com.errasoft.friendfinder.service.impl;

import com.errasoft.friendfinder.controller.vm.ContactInfoResponseVm;
import com.errasoft.friendfinder.dto.ContactInfoDto;
import com.errasoft.friendfinder.dto.security.AccountDto;
import com.errasoft.friendfinder.mapper.ContactInfoMapper;
import com.errasoft.friendfinder.mapper.security.AccountMapper;
import com.errasoft.friendfinder.model.ContactInfo;
import com.errasoft.friendfinder.model.security.Account;
import com.errasoft.friendfinder.repo.ContactInfoRepo;
import com.errasoft.friendfinder.repo.security.AccountRepo;
import com.errasoft.friendfinder.service.ContactInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class ContactInfoServiceImpl implements ContactInfoService {

    private ContactInfoRepo contactInfoRepo;
    private ContactInfoMapper contactInfoMapper;
    private AccountMapper accountMapper;
    private AccountRepo accountRepo;

    @Autowired
    public ContactInfoServiceImpl(ContactInfoRepo contactInfoRepo,
                                  ContactInfoMapper contactInfoMapper,
                                  AccountMapper accountMapper,
                                  AccountRepo accountRepo) {
        this.contactInfoRepo = contactInfoRepo;
        this.contactInfoMapper = contactInfoMapper;
        this.accountMapper = accountMapper;
        this.accountRepo = accountRepo;
    }

    @Override
    public ContactInfoDto createContactInfo(ContactInfoDto contactInfoDto) {

        if (Objects.nonNull(contactInfoDto.getId())) {
            throw new RuntimeException("id.must_be.null");
        }

        ContactInfo contactInfo = contactInfoMapper.toContactInfo(contactInfoDto);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AccountDto accountDto = (AccountDto)authentication.getPrincipal();

        if (accountDto.getAccountDetails() == null) {
            throw new RuntimeException("account.details.not.found");
        }

        Account account = accountMapper.toAccount(accountDto);

        contactInfo.setAccount(account);

        ContactInfo savedContact = contactInfoRepo.save(contactInfo);

        return contactInfoMapper.toContactInfoDto(savedContact);
    }

    @Override
    public ContactInfoResponseVm getAllContacts(int page, int size) {
        Pageable pageable = getPageable(page, size);
        Page<ContactInfo> contacts = contactInfoRepo.findAllByOrderByCreatedDateDesc(pageable);
       if(contacts.isEmpty()){
           throw new RuntimeException("contacts.not.found");
       }
       List<ContactInfoDto> contactInfoDtos = contactInfoMapper.toContactInfoDtoList(contacts.getContent());
       return new ContactInfoResponseVm(contactInfoDtos,contacts.getTotalElements());
    }

    @Override
    public ContactInfoResponseVm getContactsByEmail(String email,int page, int size) {
        Pageable pageable = getPageable(page, size);
        Account account = accountRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("account.username.notExists"));

        Page<ContactInfo> contacts = contactInfoRepo.findByAccountOrderByCreatedDateDesc(account,pageable);

        if(contacts.isEmpty()){
            throw new RuntimeException("contacts.not.found");
        }
        
        List<ContactInfoDto> contactInfoDtos = contactInfoMapper.toContactInfoDtoList(contacts.getContent());
        return new ContactInfoResponseVm(contactInfoDtos,contacts.getTotalElements());
    }

    @Override
    public ContactInfoDto updateMessage(ContactInfoDto contactInfoDto) {

        Account user;
        ContactInfo existing = contactInfoRepo.findById(contactInfoDto.getId())
                .orElseThrow(() -> new RuntimeException("contact.not.found"));

        ContactInfo updatedEntity = contactInfoMapper.toContactInfo(contactInfoDto);
        updatedEntity.setId(existing.getId());
        updatedEntity.setCreatedDate(existing.getCreatedDate());
        updatedEntity.setUpdatedDate(LocalDateTime.now());
        updatedEntity.setAccount(existing.getAccount());
        ContactInfo saved = contactInfoRepo.save(updatedEntity);

        return contactInfoMapper.toContactInfoDto(saved);
    }

    @Override
    public ContactInfoResponseVm searchContacts(String keyword, int page, int size) {

        Pageable pageable = getPageable(page, size);

        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllContacts(page, size);
        }

        List<ContactInfo> contacts = contactInfoRepo.searchContacts(keyword.trim());

        if (contacts.isEmpty()) {
            throw new RuntimeException("contacts.not.found");
        }

        long total = contacts.size();
        int totalPages = (int) Math.ceil((double) total / size);
        if(page > totalPages){
            page = 1;
        }

        int start = (page - 1) * size;
        int end = Math.min(start + size, contacts.size());
        if (start >= contacts.size()) {
            throw new RuntimeException("contacts.not.found");
        }

        List<ContactInfo> paginatedList = contacts.subList(start, end);

        List<ContactInfoDto> contactInfoDtos = contactInfoMapper.toContactInfoDtoList(paginatedList);

        return new ContactInfoResponseVm(contactInfoDtos,  (long) contacts.size());
    }

    private static Pageable getPageable(int page, int size) {
        try {
            if (page < 1) {
                throw new RuntimeException("error.min.one.page");
            }
            return PageRequest.of(page - 1, size);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


}
