package com.errasoft.friendfinder.dto.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountDetailsDto implements Serializable {

    private Long id;

    @NotEmpty(message = "not_empty.phone_number")
    private String phoneNumber;

    @NotNull(message = "not_empty.age")
    @Min(value = 16, message = "error.age.min")
    @Max(value = 80, message = "error.age.max")
    private Integer age;

    @NotEmpty(message = "not_empty.address")
    private String address;

    @JsonIgnore
    private AccountDto account;
}
