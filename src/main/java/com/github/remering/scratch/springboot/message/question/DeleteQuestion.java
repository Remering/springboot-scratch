package com.github.remering.scratch.springboot.message.question;

import com.github.remering.scratch.springboot.bean.ResponseCode;
import com.github.remering.scratch.springboot.message.CodeMessageResponse;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

public class DeleteQuestion {

    private DeleteQuestion() {
    }

    @NotBlank
    @Validated
    @Data
    public static class Request {
        private UUID uuid;
    }

    @Data
    @Builder
    public static class Response implements CodeMessageResponse {
        private final ResponseCode code;
        private final String message;
    }
}
