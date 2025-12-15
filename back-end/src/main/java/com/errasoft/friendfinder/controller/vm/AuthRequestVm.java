package com.errasoft.friendfinder.controller.vm;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class AuthRequestVm {

   // @NotEmpty(message = "not_empty.username")
    @Size(min = 7, message = "size.username")
    private String name;

    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z\\d]).{7,}$",
            message = "auth.error.password"
    )
    private String password;

    @NotEmpty(message = "not_empty.email")
    @Email(message = "not_valid.email")
    private String email;
}
