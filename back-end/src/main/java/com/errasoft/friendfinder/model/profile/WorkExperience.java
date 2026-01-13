package com.errasoft.friendfinder.model.profile;

import com.errasoft.friendfinder.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "work_experiences")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkExperience extends BaseEntity {

    private String companyName;

    private String jobTitle;

    private LocalDate startDate;

    private LocalDate endDate;

    private boolean currentJob;

    private boolean deleted = false;

    @ManyToOne
    @JoinColumn(name = "about_profile_id")
    private AboutProfile aboutProfile;
}
