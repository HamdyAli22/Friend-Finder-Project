package com.errasoft.friendfinder.controller.vm;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FriendResponseVM {

    private Long id;

    private Long friendId;

    private String username;

    private String bio;

    private String profileImageUrl;

    private String coverImageUrl;

}
