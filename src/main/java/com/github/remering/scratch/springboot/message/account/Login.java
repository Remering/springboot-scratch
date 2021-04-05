package com.github.remering.scratch.springboot.message.account;

import com.github.remering.scratch.springboot.bean.ResponseCode;
import com.github.remering.scratch.springboot.message.CodeMessageResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

public class Login {

    private Login() {
    }

    @Data
    @Validated
    @AllArgsConstructor
    @NoArgsConstructor
    @NotEmpty
    public static class Request {
        @NotBlank
        private String account;
        @Length(min = 64, max = 64)
        private String password;
    }

    @Data
    @Builder
    public static class Response implements CodeMessageResponse {
        private final ResponseCode code;
        private final String message;
        @Nullable
        private final String token;
    }
}
