package com.errasoft.friendfinder.mapper;

import com.errasoft.friendfinder.dto.ReactionDto;
import com.errasoft.friendfinder.model.Reaction;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReactionMapper {

    Reaction toReaction(ReactionDto reactionDto);

    ReactionDto toReactionDto(Reaction reaction);

    List<Reaction>  toReactionList(List<ReactionDto> reactionDtoList);

    List<ReactionDto> toReactionDtoList(List<Reaction> reactions);

}
