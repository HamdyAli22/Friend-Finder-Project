package com.errasoft.friendfinder.controller.profile;

import com.errasoft.friendfinder.dto.profile.LanguageDto;
import com.errasoft.friendfinder.service.profile.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/about-profile/language")
public class LanguageController {

    private  LanguageService languageService;

    @Autowired
    public LanguageController(LanguageService languageService) {
        this.languageService = languageService;
    }

    @PostMapping("/create-language")
    public ResponseEntity<LanguageDto> createLanguage(@RequestBody LanguageDto dto) {
        return ResponseEntity.ok(languageService.createLanguage(dto));
    }


    @PutMapping("/update-language")
    public ResponseEntity<LanguageDto> updateLanguage(@RequestBody LanguageDto dto) {
        return ResponseEntity.ok(languageService.updateLanguage(dto));
    }


    @GetMapping("/get-language")
    public ResponseEntity<List<LanguageDto>> getLanguagesByAccountId(@RequestParam Long accountId) {
        return ResponseEntity.ok(languageService.getLanguagesByAccountId(accountId));
    }


    @DeleteMapping("/delete-language")
    public ResponseEntity<Void> deleteLanguage(@RequestParam(required = false) Long languageId) {
        languageService.deleteLanguage(languageId);
        return ResponseEntity.noContent().build();
    }
}
