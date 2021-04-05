package com.github.remering.scratch.springboot.bean.message;

import lombok.Builder;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.net.URI;
import java.util.UUID;

@Data
@Builder
public class AccountWithAvatarMessage {
    private UUID uuid;
    private String username;
    @Nullable
    private URI avatarUrl;
}
