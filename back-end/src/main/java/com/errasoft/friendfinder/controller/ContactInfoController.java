package com.errasoft.friendfinder.controller;

import com.errasoft.friendfinder.controller.vm.ContactInfoResponseVm;
import com.errasoft.friendfinder.dto.ContactInfoDto;
import com.errasoft.friendfinder.service.ContactInfoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contact")
public class ContactInfoController {

    private ContactInfoService contactInfoService;

    @Autowired
    public ContactInfoController(ContactInfoService contactInfoService) {
        this.contactInfoService = contactInfoService;
    }

    @PostMapping("/save-contact")
    public ResponseEntity<ContactInfoDto> saveContact(@RequestBody @Valid ContactInfoDto contactInfoDto) {
        ContactInfoDto savedContact = contactInfoService.createContactInfo(contactInfoDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedContact);
    }

    @GetMapping("/all-contacts")
    public ResponseEntity<ContactInfoResponseVm> getAllContacts(@RequestParam int page, @RequestParam int size) {
        ContactInfoResponseVm contacts = contactInfoService.getAllContacts(page, size);
        return ResponseEntity.ok(contacts);
    }

    @GetMapping("/user-contacts")
    public ResponseEntity<ContactInfoResponseVm> getContactsByUsername(@RequestParam String email,@RequestParam int page, @RequestParam int size) {
        ContactInfoResponseVm contacts = contactInfoService.getContactsByEmail(email,page,size);
        return ResponseEntity.ok(contacts);
    }

    @PutMapping("/update-message")
    public ResponseEntity<ContactInfoDto> updateMessage(@RequestBody @Valid ContactInfoDto contactInfoDto) {
        ContactInfoDto updated = contactInfoService.updateMessage(contactInfoDto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/search")
    public ResponseEntity<ContactInfoResponseVm> searchContacts(
            @RequestParam String keyword,
            @RequestParam int page,
            @RequestParam int size) {

        ContactInfoResponseVm contacts = contactInfoService.searchContacts(keyword, page, size);
        return ResponseEntity.ok(contacts);
    }


}
