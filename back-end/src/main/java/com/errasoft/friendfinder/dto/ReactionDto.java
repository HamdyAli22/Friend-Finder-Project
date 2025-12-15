package com.errasoft.friendfinder.dto;

import com.errasoft.friendfinder.dto.security.AccountDto;
import com.errasoft.friendfinder.utils.ReactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReactionDto {
    private Long id;
    private Long postId;
    private AccountDto user;
    private ReactionType type;
    private LocalDateTime createdDate;
}
