package com.github.remering.scratch.springboot.message.question;

import com.github.remering.scratch.springboot.bean.ResponseCode;
import com.github.remering.scratch.springboot.bean.message.QuestionMessage;
import com.github.remering.scratch.springboot.message.CodeMessageResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.UUID;

public class AddQuestion {

    private AddQuestion() {
    }

    @Data
    @NotBlank
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private UUID course;
        private String text;
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
