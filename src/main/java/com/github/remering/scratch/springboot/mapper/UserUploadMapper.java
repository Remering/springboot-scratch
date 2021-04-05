package com.github.remering.scratch.springboot.mapper;

import com.github.remering.scratch.springboot.entity.UserUploadEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.UUID;

@Mapper
public interface UserUploadMapper {

    UserUploadEntity getBySha256(@Param("sha256") byte[] sha256);

    int add(UserUploadEntity entity);

    int deleteBySha256(@Param("sha256") byte[] sha256);

}
