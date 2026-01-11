package com.errasoft.friendfinder.controller.vm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReactionUsersVm {
    private List<String> likes;
    private List<String> dislikes;
}
