package com.github.remering.scratch.springboot.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseEntity {
    private UUID uuid;
    private String name;
    private AccountEntity teacher;
    private String introduction;
    private UserUploadEntity picture;
    private UserUploadEntity file;
    private UserUploadEntity video;
}
