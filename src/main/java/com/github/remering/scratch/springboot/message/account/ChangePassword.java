package com.github.remering.scratch.springboot.message.account;

import com.github.remering.scratch.springboot.bean.ResponseCode;
import com.github.remering.scratch.springboot.message.CodeMessageResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class ChangePassword {

    private ChangePassword() {
    }

    @Data
    @Validated
    @AllArgsConstructor
    @NoArgsConstructor
    @NotEmpty
    @Length(min = 64, max = 64)
    public static class Request {
        @Email
        private String email;
        private String oldPassword;
        private String newPassword;
        private String newPasswordConfirm;
    }

    @Data
    @Builder
    public static class Response implements CodeMessageResponse {
        private final ResponseCode code;
        private final String message;
    }
}
