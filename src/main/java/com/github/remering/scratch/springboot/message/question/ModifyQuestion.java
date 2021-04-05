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
import javax.validation.constraints.PastOrPresent;
import java.time.Instant;
import java.util.UUID;

public class ModifyQuestion {

    private ModifyQuestion() {
    }

    @Data
    @Validated
    @AllArgsConstructor
    @NoArgsConstructor
    @NotBlank
    public static class Request {
        private UUID uuid;
        private String text;
        @PastOrPresent
        private Instant issueAt;
    }

    @Data
    @Builder
    public static class Response implements CodeMessageResponse {
        private final ResponseCode code;
        private final String message;
        private final QuestionMessage question;
    }
}
