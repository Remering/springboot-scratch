package com.github.remering.scratch.springboot.service;

import com.github.remering.scratch.springboot.bean.ResponseCode;
import com.github.remering.scratch.springboot.bean.ResponseMessage;
import com.github.remering.scratch.springboot.entity.AccountEntity;
import com.github.remering.scratch.springboot.entity.AvatarEntity;
import com.github.remering.scratch.springboot.entity.UserUploadEntity;
import com.github.remering.scratch.springboot.mapper.AccountMapper;
import com.github.remering.scratch.springboot.message.account.Register;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import lombok.var;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Arrays;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class AccountService {

    private final AccountMapper accountMapper;

    private final AvatarService avatarService;

    private final UserUploadService userUploadService;

    private final RedisService redisService;

    public AccountEntity getByUUID(UUID uuid) {
        return accountMapper.getByUUID(uuid);
    }

    @Transactional
    public Register.Response register(AccountEntity account) {

        try {
            val result = accountMapper.add(account);
            if (result != 1) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return Register.Response.builder()
                        .code(ResponseCode.ERROR)
                        .message(ResponseMessage.RETRY_LATER)
                        .build();
            }
        } catch (SQLException exception) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            val message = exception.getCause() instanceof SQLIntegrityConstraintViolationException
                    ? ResponseMessage.EMAIL_ALREADY_IN_USE
                    : ResponseMessage.INTERNAL_SERVER_ERROR;
            if (message.equals(ResponseMessage.INTERNAL_SERVER_ERROR)) {
                log.error("Exception thrown on register account with username = {} email = {}", account.getUsername(), account.getEmail(), exception);
            }

            return Register.Response.builder()
                    .code(ResponseCode.ERROR)
                    .message(message)
                    .build();
        }


        return Register.Response.builder()
                .code(ResponseCode.SUCCESS)
                .message(ResponseMessage.OK)
                .build();
    }

    public AccountEntity login(String email, byte[] password) {
        return accountMapper.login(email, password);
    }

    @SneakyThrows
    @Transactional(rollbackFor = IllegalStateException.class)
    public void resetPassword(String email, byte[] password) {
        val result = accountMapper.resetPassword(email, password);
        if (result > 1) throw new IllegalStateException("reset password returns " + result + ", which is greater that 1");
    }

    @SneakyThrows
    @Transactional(rollbackFor = IllegalStateException.class)
    public boolean changePassword(String email, byte[] oldPassword, byte[] newPassword) {
        val result = accountMapper.changePassword(email, oldPassword, newPassword);
        if (result > 1) throw new IllegalStateException("change password returns " + result + ", which is greater that 1");
        return result == 1;
    }

    @Nullable
    @Transactional
    public UserUploadEntity updateAvatar(AccountEntity accountEntity, MultipartFile avatar, byte[] sha256)  {
        val newUserUploadEntity = userUploadService.save(avatar, sha256);
        if (newUserUploadEntity == null) return null;
        var avatarEntity = avatarService.getByAccountUUID(accountEntity.getUuid());
        if (avatarEntity != null) {
            if (Arrays.equals(avatarEntity.getUserUpload().getSha256(), newUserUploadEntity.getSha256())) {
                return newUserUploadEntity;
            }
            val oldUserUploadEntity = avatarEntity.getUserUpload();
            avatarEntity.setUserUpload(newUserUploadEntity);
            avatarService.updateSha256(avatarEntity);
            userUploadService.delete(oldUserUploadEntity);
        } else {

            avatarEntity = AvatarEntity.builder()
                    .account(accountEntity)
                    .userUpload(newUserUploadEntity)
                    .build();
            avatarService.add(avatarEntity);
        }
        return newUserUploadEntity;
    }


}
