package com.cgs.backend.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserLoginRequest {

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식은 올바르지 않습니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다")
    private String password;
}
