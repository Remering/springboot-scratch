package com.github.remering.scratch.springboot.message.account;

import com.github.remering.scratch.springboot.bean.ResponseCode;
import com.github.remering.scratch.springboot.message.CodeMessageResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;

public class SendVerificationCode {
    private SendVerificationCode() {
    }

    @Data
    @Validated
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        @Email
        private String email;
    }

    @Data
    @Builder
    public static class Response implements CodeMessageResponse {
        private final ResponseCode code;
        private final String message;
    }
}
