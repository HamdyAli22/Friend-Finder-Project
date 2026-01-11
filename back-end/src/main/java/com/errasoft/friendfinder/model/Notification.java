package com.errasoft.friendfinder.model;

import com.errasoft.friendfinder.model.security.Account;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Notification extends BaseEntity{

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private boolean read = false;

    private String type;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(name = "post_id")
    private Long postId;

    private boolean deleted = false;
}
