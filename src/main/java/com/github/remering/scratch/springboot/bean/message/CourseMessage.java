package com.github.remering.scratch.springboot.bean.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URI;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseMessage {
    private UUID uuid;
    private String name;
    private String introduction;
    private UUID teacher;
    private URI picture;
    private URI video;
    private URI file;
}
