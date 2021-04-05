package com.github.remering.scratch.springboot.entity;

import com.github.remering.scratch.springboot.bean.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountEntity {
    private UUID uuid;
    private String username;
    @Builder.Default
    private byte[] password = null;
    private String email;
    private Role role;
}

