package com.errasoft.friendfinder.dto;

import com.errasoft.friendfinder.dto.security.AccountDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContactInfoDto {

    private Long id;

    @NotEmpty(message = "not_empty.name")
    @Size(min = 2, max = 100, message = "size.name")
    private String name;

    @NotEmpty(message = "not_empty.email")
    @Email(message = "not_valid.email")
    private String email;

    @NotEmpty(message = "not_empty.phone_number")
    @Pattern(
            regexp = "^[0-9]{10,15}$",
            message = "not_valid.phone_number"
    )
    private String phone;

    @NotEmpty(message = "not_empty.message")
    @Size(min = 10, max = 1000, message = "size.message")
    private String message;

    private AccountDto account;
}
