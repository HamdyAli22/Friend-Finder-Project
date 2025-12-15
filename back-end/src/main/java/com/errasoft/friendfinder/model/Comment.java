package com.errasoft.friendfinder.model;

import com.errasoft.friendfinder.model.security.Account;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "comments")
@Getter
@Setter
public class Comment extends BaseEntity{

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean deleted = false;


    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Account owner;

}
