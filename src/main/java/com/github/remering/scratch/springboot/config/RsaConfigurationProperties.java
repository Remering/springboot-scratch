package com.github.remering.scratch.springboot.config;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

@Data
@NoArgsConstructor
@ConfigurationProperties("jwt.rsa")
public class RsaConfigurationProperties {
    private Resource jksLocation;
    private String jksAlias;
    private String jksPassword;
}