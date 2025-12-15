package com.errasoft.friendfinder.dto.security;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto implements Serializable {

    private Long id;

    private String roleName;

}
