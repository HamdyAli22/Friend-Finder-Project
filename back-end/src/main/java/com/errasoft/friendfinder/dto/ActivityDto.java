package com.errasoft.friendfinder.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActivityDto {
    private String type;
    private String message;
    private LocalDateTime date;

    private Long postId;
    private String mediaType;
}
