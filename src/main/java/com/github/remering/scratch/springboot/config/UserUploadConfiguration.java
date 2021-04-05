package com.github.remering.scratch.springboot.config;


import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;

@Configuration
@AllArgsConstructor
@EnableConfigurationProperties(UserUploadConfigurationProperties.class)
public class UserUploadConfiguration {

    private final UserUploadConfigurationProperties properties;

    @Bean
    public FileSystemResource userUploadBaseDir() {
        return properties.getBaseDir();
    }

    @Bean
    public UriBuilder userUploadUriBuilder() {
        return UriComponentsBuilder.fromUri(properties.getHost())
                .pathSegment(properties.getBaseURL(), "{hash}");
    }

    @Bean
    public Duration userUploadCacheControlMaxAge() {
        return properties.getMaxAge();
    }

}
