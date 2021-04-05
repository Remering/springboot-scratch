package com.github.remering.scratch.springboot.message.account;

import com.github.remering.scratch.springboot.bean.Role;
import com.github.remering.scratch.springboot.bean.ResponseCode;
import com.github.remering.scratch.springboot.message.CodeMessageResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class Register {

    private Register() {
    }

    @Data
    @Validated
    @NotEmpty
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotBlank
        private String username;
        @Length(min = 64, max = 64)
        private String password;
        @Length(min = 64, max = 64)
        private String passwordConfirm;
        @Email
        private String email;
        @Length(min = 6, max = 6)
        private String verificationCode;
        @NotNull
        private Role role;
    }

    @Data
    @Builder
    public static class Response implements CodeMessageResponse {
        private final ResponseCode code;
        private final String message;
    }
}
