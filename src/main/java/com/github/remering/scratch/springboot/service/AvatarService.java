package com.github.remering.scratch.springboot.service;

import com.github.remering.scratch.springboot.bean.message.AccountWithAvatarMessage;
import com.github.remering.scratch.springboot.entity.AccountEntity;
import com.github.remering.scratch.springboot.entity.AvatarEntity;
import com.github.remering.scratch.springboot.mapper.AvatarMapper;
import lombok.AllArgsConstructor;
import lombok.val;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriBuilder;

import java.util.UUID;

@Service
@AllArgsConstructor
public class AvatarService {

    private final UserUploadService userUploadService;

    private final AvatarMapper avatarMapper;

    public void add(AvatarEntity avatarEntity) {
        avatarMapper.add(avatarEntity);
    }

    public AvatarEntity getByID(int id) {
        return avatarMapper.getByID(id);
    }

    public AvatarEntity getByAccountUUID(UUID uuid) {

        return avatarMapper.getByAccountUUID(uuid);
    }

    public AccountWithAvatarMessage attachAvatar(AccountEntity entity) {

        val avatarEntity = getByAccountUUID(entity.getUuid());

        val avatarURL = avatarEntity == null ? null : userUploadService.getURI(avatarEntity.getUserUpload());

        return AccountWithAvatarMessage.builder()
                .uuid(entity.getUuid())
                .username(entity.getUsername())
                .avatarUrl(avatarURL)
                .build();
    }

    public void updateSha256(AvatarEntity entity) {
        avatarMapper.updateSha256ByID(entity);
    }
}
