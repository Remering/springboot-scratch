package com.github.remering.scratch.springboot.message.course;

import com.github.remering.scratch.springboot.bean.ResponseCode;
import com.github.remering.scratch.springboot.bean.message.TeacherCourseMessage;
import com.github.remering.scratch.springboot.message.CodeMessageResponse;
import lombok.Builder;
import lombok.Data;

import java.util.Map;
import java.util.UUID;

public class GetCourses {

    private GetCourses() {
    }

    @Data
    @Builder
    public static class Response implements CodeMessageResponse {
        private final ResponseCode code;
        private final String message;
        private final Map<UUID, TeacherCourseMessage> courses;
    }
}
