package com.github.remering.scratch.springboot.bean.message;

import com.github.remering.scratch.springboot.entity.QuestionEntity;
import lombok.*;
import org.springframework.lang.Nullable;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class QuestionMessage {
    private UUID uuid;
    private UUID course;
    private AccountWithAvatarMessage issuer;
    private Instant issueAt;
    private String text;
    @Nullable
    @Builder.Default
    private String answer = null;
    @Nullable
    @Builder.Default
    private Instant answerAt = null;

    @Nullable
    public static QuestionMessage from(@Nullable QuestionEntity entity, AccountWithAvatarMessage accountWithAvatarMessage) {
        if (entity == null) return null;
        var builder = QuestionMessage.builder();
        builder = builder.uuid(entity.getUuid());
        if (entity.getCourse() != null) {
            builder = builder.course(entity.getCourse().getUuid());
        }
        builder = builder.issuer(accountWithAvatarMessage)
                .issueAt(entity.getIssueAt())
                .text(entity.getText())
                .answer(entity.getAnswer())
                .answerAt(entity.getAnswerAt());
        return builder.build();
    }
}
