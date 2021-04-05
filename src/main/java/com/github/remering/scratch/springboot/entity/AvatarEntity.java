package com.github.remering.scratch.springboot.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvatarEntity {
    @Builder.Default
    private Integer id = null;
    private AccountEntity account;
    private UserUploadEntity userUpload;
}
