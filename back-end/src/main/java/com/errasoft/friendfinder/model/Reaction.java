package com.errasoft.friendfinder.model;

import com.errasoft.friendfinder.model.security.Account;
import com.errasoft.friendfinder.utils.ReactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Reaction extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Account owner;

    @Enumerated(EnumType.STRING)
    private ReactionType type;

    @Column(nullable = false)
    private boolean deleted = false;
}
