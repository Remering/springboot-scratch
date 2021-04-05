package com.github.remering.scratch.springboot.bean;

import com.github.remering.scratch.springboot.entity.AccountEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class AccountPrincipal {
    private UUID uuid;
    private String username;
    private String email;
    private Role role;

    public AccountEntity toAccountEntity() {
        return AccountEntity.builder()
                .uuid(uuid)
                .username(username)
                .email(email)
                .role(role)
                .build();
    }
}
