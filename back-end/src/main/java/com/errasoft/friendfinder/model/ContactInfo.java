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
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContactInfo extends BaseEntity{

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false,length = 1000)
    private String message;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
}
