package com.errasoft.friendfinder.controller.profile;

import com.errasoft.friendfinder.dto.profile.AboutProfileDto;
import com.errasoft.friendfinder.service.profile.AboutProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/about-profile")
public class AboutProfileController {

    private AboutProfileService aboutProfileService;

    @Autowired
    public AboutProfileController(AboutProfileService aboutProfileService) {
        this.aboutProfileService = aboutProfileService;
    }

    @PostMapping("/create-profile")
    public ResponseEntity<AboutProfileDto> createAboutProfile(@RequestBody AboutProfileDto aboutProfileDto) {
        return ResponseEntity.ok(aboutProfileService.createAboutProfile(aboutProfileDto));
    }

    @PutMapping("/update-profile")
    public ResponseEntity<AboutProfileDto> updateAboutProfile(@RequestBody AboutProfileDto aboutProfileDto) {
        return ResponseEntity.ok(aboutProfileService.updateAboutProfile(aboutProfileDto));
    }


    @GetMapping("get-profile")
    public ResponseEntity<AboutProfileDto> getAboutProfileByAccountId(@RequestParam Long accountId) {
        return ResponseEntity.ok(aboutProfileService.getAboutProfileByAccountId(accountId));
    }


    @DeleteMapping("/delete-profile")
    public ResponseEntity<Void> deleteAboutProfile(@RequestParam Long aboutProfileId) {
        aboutProfileService.deleteAboutProfile(aboutProfileId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete-personal-info")
    public ResponseEntity<Void> deletePersonalInfo(@RequestParam Long aboutProfileId) {
        aboutProfileService.deletePersonalInfo(aboutProfileId);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/delete-address")
    public ResponseEntity<Void> deleteAddress(@RequestParam Long aboutProfileId) {
        aboutProfileService.deleteAddress(aboutProfileId);
        return ResponseEntity.noContent().build();
    }
}
