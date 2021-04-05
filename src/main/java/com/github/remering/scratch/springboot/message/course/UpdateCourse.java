package com.github.remering.scratch.springboot.message.course;

import com.github.remering.scratch.springboot.bean.ResponseCode;
import com.github.remering.scratch.springboot.bean.message.CourseMessage;
import com.github.remering.scratch.springboot.message.CodeMessageResponse;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.Nullable;

public class UpdateCourse {

    @Data
    @Builder
    public static class Response implements CodeMessageResponse {
        private final ResponseCode code;
        private final String message;
        @Nullable
        private final CourseMessage course;
    }
}
