package com.errasoft.friendfinder.controller.vm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangePassReqVM {

    //@NotBlank(message = "old.pass.required")
    private String oldPassword;

   // @NotBlank(message = "new.pass.required")
//    @Pattern(
//            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z\\d]).{7,}$",
//            message = "new.pass.regexp"
//    )
    private String newPassword;

   // @NotBlank(message = "confirm.pass.required")
    private String confirmPassword;

    private String email;
}
