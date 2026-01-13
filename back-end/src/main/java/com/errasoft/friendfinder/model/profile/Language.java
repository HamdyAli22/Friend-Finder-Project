package com.errasoft.friendfinder.model.profile;

import com.errasoft.friendfinder.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "languages")
public class Language extends BaseEntity {


    private String name;

    private boolean deleted = false;

    @ManyToOne
    @JoinColumn(name = "about_profile_id")
    private AboutProfile aboutProfile;
}
