package com.errasoft.friendfinder.mapper;

import com.errasoft.friendfinder.dto.PostDto;
import com.errasoft.friendfinder.mapper.security.AccountMapper;
import com.errasoft.friendfinder.model.Post;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {AccountMapper.class})
public interface PostMapper {

    Post toPost(PostDto postDto);

    PostDto toPostDto(Post post);

    List<Post>  toPostList(List<PostDto> postDtoList);

    List<PostDto> toPostDtoList(List<Post> posts);

}
