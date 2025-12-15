package com.errasoft.friendfinder.mapper;

import com.errasoft.friendfinder.dto.FriendshipDto;
import com.errasoft.friendfinder.model.Friendship;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FriendshipMapper {


    Friendship toFriendship(FriendshipDto friendshipDto);

    @Mapping(source = "requester.id", target = "requesterId")
    @Mapping(source = "receiver.id", target = "receiverId")
    FriendshipDto toFriendshipDto(Friendship friendship);

    List<Friendship>  toFriendshipList(List<FriendshipDto> friendshipDtoList);

    List<FriendshipDto> toFriendshipDtoList(List<Friendship> friendshipList);
}
