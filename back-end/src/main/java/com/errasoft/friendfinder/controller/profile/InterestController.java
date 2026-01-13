package com.errasoft.friendfinder.controller.profile;

import com.errasoft.friendfinder.dto.profile.InterestDto;
import com.errasoft.friendfinder.service.profile.InterestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/about-profile/interest")
public class InterestController {

    private InterestService interestService;

    @Autowired
    public InterestController(InterestService interestService) {
        this.interestService = interestService;
    }


    @PostMapping("/create-interest")
    public ResponseEntity<InterestDto> createInterest(@RequestBody InterestDto dto) {
        return ResponseEntity.ok(interestService.createInterest(dto));
    }


    @PutMapping("/update-interest")
    public ResponseEntity<InterestDto> updateInterest(@RequestBody InterestDto dto) {
        return ResponseEntity.ok(interestService.updateInterest(dto));
    }


    @GetMapping("get-interest")
    public ResponseEntity<List<InterestDto>> getInterestsByAccountId(@RequestParam Long accountId) {
        return ResponseEntity.ok(interestService.getInterestsByAccountId(accountId));
    }

    // Delete (soft delete) using request param
    @DeleteMapping("/delete-interest")
    public ResponseEntity<Void> deleteInterest(@RequestParam Long interestId) {
        interestService.deleteInterest(interestId);
        return ResponseEntity.noContent().build();
    }
}
