package com.github.remering.scratch.springboot.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionEntity {
    @Builder.Default
    private UUID uuid = UUID.randomUUID();
    @Nullable
    @Builder.Default
    private CourseEntity course = null;
    private AccountEntity issuer;
    @Nullable
    @Builder.Default
    private Instant issueAt = null;
    private String text;
    @Nullable
    @Builder.Default
    private String answer = null;
    @Nullable
    @Builder.Default
    private Instant answerAt = null;
}
