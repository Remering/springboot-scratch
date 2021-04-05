package com.github.remering.scratch.springboot.config;

import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@AllArgsConstructor
@EnableConfigurationProperties(VerificationCodeConfigurationProperties.class)
public class VerificationCodeConfiguration {
    private final VerificationCodeConfigurationProperties properties;

    @Bean
    public String verificationCodeEmailFrom() {
        return properties.getFrom();
    }

    @Bean
    public Duration verificationCodeMinSendInterval() {
        return properties.getMinSendInterval();
    }

    @Bean
    public Duration verificationCodeExpireTime() {
        return properties.getExpireTime();
    }


}
