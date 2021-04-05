package com.github.remering.scratch.springboot.mapper;

import com.github.remering.scratch.springboot.entity.AccountEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.UUID;

@Mapper
public interface AccountMapper {

    AccountEntity getByUUID(UUID uuid);

    int add(AccountEntity entity) throws SQLException;

    int deleteByUUID(UUID uuid);

    AccountEntity login(@Param("email") String email, @Param("password") byte[] password);

    int resetPassword(@Param("email") String email, @Param("newPassword") byte[] newPassword);

    int changePassword(@Param("email") String email, @Param("oldPassword") byte[] oldPassword, @Param("newPassword") byte[] newPassword);

    int updateByUUID(AccountEntity entity);
}
