package com.errasoft.friendfinder.dto;

import com.errasoft.friendfinder.dto.security.AccountDto;
import com.errasoft.friendfinder.utils.MediaType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {

    private Long id;

   // @NotEmpty(message = "post.content.required")
    @Size(max = 1000, message = "post.content.size")
    private String content;

    private String mediaUrl;

    private MediaType mediaType;

    private AccountDto owner;

    private Long likesCount = 0L;

    private Long commentsCount = 0L;

    private Long dislikesCount = 0L;

    private LocalDateTime createdDate;
}
