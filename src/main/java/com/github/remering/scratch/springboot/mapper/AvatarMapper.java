package com.github.remering.scratch.springboot.mapper;

import com.github.remering.scratch.springboot.entity.AvatarEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.UUID;

@Mapper
public interface AvatarMapper {

    AvatarEntity getByID(int id);

    AvatarEntity getByAccountUUID(UUID uuid);

    void add(AvatarEntity entity);

    int updateSha256ByID(AvatarEntity entity);

}
