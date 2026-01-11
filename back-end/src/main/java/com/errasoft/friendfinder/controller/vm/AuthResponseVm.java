package com.errasoft.friendfinder.controller.vm;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseVm {

    private long userId;
    private String username;
    private String email;
    private String token;
    @JsonProperty("roles")
    private List<String> userRoles;
}
