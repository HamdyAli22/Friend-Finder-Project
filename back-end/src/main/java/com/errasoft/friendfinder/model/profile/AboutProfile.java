package com.errasoft.friendfinder.model.profile;

import com.errasoft.friendfinder.model.BaseEntity;
import com.errasoft.friendfinder.model.security.Account;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AboutProfile extends BaseEntity {

    private String personalInfo;

    private String address;

    private boolean deleted = false;

    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToMany(mappedBy = "aboutProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkExperience> workExperiences = new ArrayList<>();


    @OneToMany(mappedBy = "aboutProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Interest> interests = new ArrayList<>();


    @OneToMany(mappedBy = "aboutProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Language> languages = new ArrayList<>();

}
