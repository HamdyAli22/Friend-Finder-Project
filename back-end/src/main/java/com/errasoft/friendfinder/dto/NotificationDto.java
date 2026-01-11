package com.errasoft.friendfinder.dto;


import com.errasoft.friendfinder.dto.security.AccountDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NotificationDto {
    private Long id;
    private String message;
    private boolean read;
    private AccountDto account;
    private LocalDateTime createdDate;
    private String type;
    private Long postId;
}
