package com.errasoft.friendfinder.mapper;

import com.errasoft.friendfinder.dto.CommentDto;
import com.errasoft.friendfinder.mapper.security.AccountMapper;
import com.errasoft.friendfinder.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = AccountMapper.class)
public interface CommentMapper {

    Comment toComment(CommentDto commentDto);

    @Mapping(source = "post.id", target = "postId")
    @Mapping(source = "owner", target = "owner")
    CommentDto toCommentDto(Comment comment);

    List<Comment> toCommentList(List<CommentDto> commentDtoList);

    List<CommentDto> toCommentDtoList(List<Comment> comments);
}
