package com.errasoft.friendfinder.controller.vm;

import com.errasoft.friendfinder.dto.CommentDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseVm {
    private List<CommentDto> comments;
    private long totalElements;
}
