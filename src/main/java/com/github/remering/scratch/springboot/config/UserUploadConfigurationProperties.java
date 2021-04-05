package com.github.remering.scratch.springboot.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.FileSystemResource;

import java.net.URI;
import java.time.Duration;

@Data
@NoArgsConstructor
@ConfigurationProperties("user-upload")
public class UserUploadConfigurationProperties {

    private FileSystemResource baseDir;

    private URI host;

    private String baseURL;

    private Duration maxAge;
}
