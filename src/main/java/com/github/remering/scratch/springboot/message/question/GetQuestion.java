package com.github.remering.scratch.springboot.message.question;

import com.github.remering.scratch.springboot.bean.ResponseCode;
import com.github.remering.scratch.springboot.bean.message.QuestionMessage;
import com.github.remering.scratch.springboot.message.CodeMessageResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.util.Map;
import java.util.UUID;

public class GetQuestion {

    private GetQuestion() {
    }

    @Validated
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @NotBlank
    public static class Request {
        private UUID course;
    }

    @Data
    @Builder
    public static class Response implements CodeMessageResponse {
        private final ResponseCode code;
        private final String message;
        private final Map<UUID, QuestionMessage> questions;
    }
}
