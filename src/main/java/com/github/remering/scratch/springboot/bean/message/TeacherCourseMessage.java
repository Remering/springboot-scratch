package com.github.remering.scratch.springboot.bean.message;

import lombok.Builder;
import lombok.Data;

import java.net.URI;
import java.util.UUID;

@Data
@Builder
public class TeacherCourseMessage {
    private UUID uuid;
    private String name;
    private String introduction;
    private URI picture;
    private AccountWithAvatarMessage teacher;
    private URI video;
    private URI file;
}
