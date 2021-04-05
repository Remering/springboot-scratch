package com.github.remering.scratch.springboot.controller;

import com.github.remering.scratch.springboot.service.UserUploadService;
import com.github.remering.scratch.springboot.util.Codec;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.time.Duration;

@Controller
public class UserUploadController {

    private final UserUploadService userUploadService;
    private final Duration maxAge;

    public UserUploadController(
            UserUploadService userUploadService,
            @Qualifier("userUploadCacheControlMaxAge")
            Duration maxAge
    ) {
        this.userUploadService = userUploadService;
        this.maxAge = maxAge;
    }

    @SneakyThrows
    @ResponseBody
    @GetMapping("/user-upload/{hash:[A-F0-9]{64}}")
    public ResponseEntity<Resource> getUserUploadWithExtension(@PathVariable String hash) {
        val userUploadEntity = userUploadService.getBySha256(Codec.hexStr2Bytes(hash));
        if (userUploadEntity == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        val fileResource = new FileSystemResource(userUploadService.getUserUploadPath(userUploadEntity.getUuid()));
        if (!fileResource.exists()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.status(HttpStatus.OK)

                .contentType(userUploadEntity.getType())
                .contentLength(fileResource.contentLength())
                .cacheControl(CacheControl.maxAge(maxAge).cachePublic())
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        String.format(
                                "inline;filename:\"%s\";filename*:UTF-8''\"%s\"",
                                URLEncoder.encode(userUploadEntity.getFilename()),
                                userUploadEntity.getFilename()
                        )
                )

                .eTag(hash)
                .body(fileResource);
    }

}
