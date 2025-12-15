package com.errasoft.friendfinder.model;

import com.errasoft.friendfinder.model.security.Account;
import com.errasoft.friendfinder.utils.FriendshipStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "friendships")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Friendship extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "requester_id")
    private Account requester;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private Account receiver;

    @Enumerated(EnumType.STRING)
    private FriendshipStatus status;
}
