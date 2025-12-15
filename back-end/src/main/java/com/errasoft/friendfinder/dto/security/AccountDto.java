package com.errasoft.friendfinder.dto.security;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountDto implements Serializable {
    private Long id;

    @NotEmpty(message = "not_empty.username")
    @Size(min = 7, message = "size.username")
    private String username;

    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z\\d]).{7,}$",
            message = "auth.error.password"
    )
    private String password;

    private boolean enabled;

    @NotEmpty(message = "not_empty.email")
    @Email(message = "not_valid.email")
    private String email;

    List<RoleDto> roles;

    private AccountDetailsDto accountDetails;
}
