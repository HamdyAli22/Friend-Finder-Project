package com.errasoft.friendfinder.dto;

import com.errasoft.friendfinder.model.security.Account;
import com.errasoft.friendfinder.utils.FriendshipStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FriendshipDto {

    private Long id;

    private Long requesterId;

    private Long receiverId;

    private FriendshipStatus status;
}
