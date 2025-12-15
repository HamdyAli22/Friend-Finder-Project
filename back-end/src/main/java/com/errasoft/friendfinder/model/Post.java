package com.errasoft.friendfinder.model;

import com.errasoft.friendfinder.model.security.Account;
import com.errasoft.friendfinder.utils.MediaType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Post extends BaseEntity {


    private String content;

    private String mediaUrl;

    @Enumerated(EnumType.STRING)
    private MediaType mediaType = MediaType.NONE;


    @Column(nullable = false)
    private boolean deleted = false;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Account owner;

    private Long likesCount = 0L;
    private Long dislikesCount = 0L;
    private Long commentsCount = 0L;
}
