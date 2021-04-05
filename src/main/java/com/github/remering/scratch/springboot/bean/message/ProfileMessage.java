package com.github.remering.scratch.springboot.bean.message;

import com.github.remering.scratch.springboot.bean.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.net.URI;
import java.net.URL;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class ProfileMessage {
    private UUID uuid;
    private String username;
    @Nullable
    private URI avatarUrl;
    private Role role;
    private String email;
}
