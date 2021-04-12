package com.github.remering.scratch.springboot.service;

import com.github.remering.scratch.springboot.entity.UserUploadEntity;
import com.github.remering.scratch.springboot.mapper.UserUploadMapper;
import lombok.AllArgsConstructor;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriBuilder;

import java.net.URI;
import java.nio.channels.Channels;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.UUID;

import static com.github.remering.scratch.springboot.util.Codec.bytes2HexStr;
import static com.github.remering.scratch.springboot.util.Codec.calculateSha256AndWrite;

@Service
@AllArgsConstructor
public class UserUploadService {

    private final UserUploadMapper userUploadMapper;

    private final FileSystemResource baseDir;

    private final UriBuilder userUploadUriBuilder;

    public UserUploadEntity getBySha256(byte[] sha256) {
        return userUploadMapper.getBySha256(sha256);
    }



    @Nullable
    public URI getURI(UserUploadEntity entity) {
        if (entity == null) return null;
        return userUploadUriBuilder.build(bytes2HexStr(entity.getSha256()));
    }


    public Path getUserUploadPath(UUID userUploadUUID) {
        return Paths.get(baseDir.getPath(), userUploadUUID.toString());
    }

    @Nullable
    @SneakyThrows
    @Transactional
    public UserUploadEntity save(MultipartFile userUploadFile, byte[] sha256) {

        // copy from multipart file to local file system
        val uuid = UUID.randomUUID();
        val path = getUserUploadPath(uuid);
        Assert.state(!Files.exists(path), "File " + path.toAbsolutePath().toString() + "already exists!");
        @Cleanup val userUploadChannel = Channels.newChannel(userUploadFile.getInputStream());
        @Cleanup val fileChannel = Files.newByteChannel(path, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
        val sha256Calculated = calculateSha256AndWrite(userUploadChannel, fileChannel);
        if (!Arrays.equals(sha256, sha256Calculated)) {
            Files.delete(path);
            return null;
        }

        // add entity to database
        val entity = UserUploadEntity.builder()
                .uuid(uuid)
                .sha256(sha256)
                .filename(userUploadFile.getOriginalFilename())
                .type(MediaType.parseMediaType(userUploadFile.getContentType()))
                .build();

        val result = userUploadMapper.add(entity);
        if (result == 1) {
            return entity;
        }

        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        Files.delete(path);
        return null;
    }

    @SneakyThrows
    @Transactional
    public void delete(UserUploadEntity entity, boolean checkExist) {

        // delete entity in database
        userUploadMapper.deleteBySha256(entity.getSha256());

        // delete file in local file system
        val fileResource = baseDir.createRelative(entity.getUuid().toString());
        if (checkExist) {
            Assert.state(fileResource.exists(), "Try to delete a non-existing file " + fileResource.getFilename());
        }
        val path = Paths.get(fileResource.getURI());
        Files.delete(path);
    }

    public void delete(UserUploadEntity entity) {
        delete(entity, true);
    }

}
