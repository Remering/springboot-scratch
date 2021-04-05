package com.github.remering.scratch.springboot.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Data
@NoArgsConstructor
@ConfigurationProperties("verification-code")
public class VerificationCodeConfigurationProperties {
    private String from;
    private Duration minSendInterval;
    private Duration expireTime;
}
