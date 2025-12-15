package com.errasoft.friendfinder.controller.vm;

import com.errasoft.friendfinder.utils.MediaType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MediaVM {
    private Long postId;
    private MediaType mediaType;
    private String mediaUrl;
}
