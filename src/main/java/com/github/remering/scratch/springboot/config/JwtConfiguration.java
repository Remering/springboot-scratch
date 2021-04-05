package com.github.remering.scratch.springboot.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;

import static com.github.remering.scratch.springboot.config.JwtConfigurationProperties.ISSUER;

@Configuration(proxyBeanMethods = false)
@AllArgsConstructor
@AutoConfigureAfter(RsaConfiguration.class)
@EnableConfigurationProperties(JwtConfigurationProperties.class)
public class JwtConfiguration {

    private final JwtConfigurationProperties properties;

    @Bean
    public Algorithm rsaAlgorithm(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
        return Algorithm.RSA256(publicKey, privateKey);
    }

    @Bean
    public Duration jwtExpireTime() {
        return properties.getExpireTime();
    }

    @Bean
    public JWTVerifier verifier(Algorithm algorithm, @Qualifier("jwtExpireTime") Duration expireTime) {
        return JWT.require(algorithm)
                .acceptLeeway(expireTime.getSeconds())
                .withIssuer(ISSUER)
                .build();
    }

}
