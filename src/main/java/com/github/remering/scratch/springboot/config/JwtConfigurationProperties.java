package com.github.remering.scratch.springboot.config;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

import java.time.Duration;

@Data
@NoArgsConstructor
@ConfigurationProperties("jwt")
public class JwtConfigurationProperties {

    public static final String ISSUER = "scratch-springboot";

    private Duration expireTime;
}