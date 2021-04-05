package com.github.remering.scratch.springboot.service;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.remering.scratch.springboot.entity.AccountEntity;
import lombok.val;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.github.remering.scratch.springboot.config.JwtConfigurationProperties.ISSUER;

@Service
public class JwtService {

    private final RedisService redisService;
    private final JWTVerifier verifier;
    private final Algorithm algorithm;
    private final Duration expireTime;

    public JwtService(
            RedisService redisService,
            JWTVerifier verifier,
            Algorithm algorithm,
            @Qualifier("jwtExpireTime")
            Duration expireTime
    ) {
        this.redisService = redisService;
        this.verifier = verifier;
        this.algorithm = algorithm;
        this.expireTime = expireTime;
    }

    @Nullable
    public DecodedJWT verifyToken(String token) {

        DecodedJWT decodedJWT;
        try {
            decodedJWT = verifier.verify(token);
        } catch (JWTVerificationException exception) {
            return null;
        }

        return redisService.validate(decodedJWT.getSubject(), decodedJWT.getKeyId()) ? decodedJWT : null;
    }

    private Map<String, Object> account2Map(AccountEntity account) {
        val hashMap = new HashMap<String, Object>();
        hashMap.put("uuid", account.getUuid().toString());
        hashMap.put("username", account.getUsername());
        hashMap.put("email", account.getEmail());
        hashMap.put("role", account.getRole().ordinal());
        return hashMap;
    }

    public String createToken(AccountEntity account) {

        val now = Instant.now(Clock.systemDefaultZone());

        val subject = account.getUuid().toString();

        val keyId = redisService.currentVersion(subject);

        return JWT.create().withSubject(subject)
                .withIssuer(ISSUER)
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(now.plus(expireTime)))
                .withPayload(account2Map(account))
                .withKeyId(keyId)
                .sign(algorithm);
    }
}
