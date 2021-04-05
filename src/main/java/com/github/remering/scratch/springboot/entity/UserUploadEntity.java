package com.github.remering.scratch.springboot.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.MediaType;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUploadEntity {
    @Builder.Default
    UUID uuid = UUID.randomUUID();
    byte[] sha256;
    String filename;
    MediaType type;
}
