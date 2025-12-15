package com.errasoft.friendfinder.dto;

import com.errasoft.friendfinder.dto.security.AccountDto;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long id;

    @NotEmpty(message = "comment.content.required")
    private String content;

    private Long postId;

    private AccountDto owner;

    private LocalDateTime createdDate;
}
