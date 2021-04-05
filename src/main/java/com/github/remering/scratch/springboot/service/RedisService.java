package com.github.remering.scratch.springboot.service;

import lombok.val;
import lombok.var;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.comparator.Comparators;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.Random;

@Service
public class RedisService {

    private final StringRedisTemplate redisTemplate;
    private final Duration jwtExpireTime;
    private final Duration minSendInterval;
    private final Duration verificationCodeExpireTime;
    private final Random generator = new Random();

    public RedisService(
            StringRedisTemplate redisTemplate,
            @Qualifier("jwtExpireTime") Duration jwtExpireTime,
            @Qualifier("verificationCodeMinSendInterval") Duration minSendInterval,
            @Qualifier("verificationCodeExpireTime") Duration verificationCodeExpireTime
    ) {
        this.redisTemplate = redisTemplate;
        this.jwtExpireTime = jwtExpireTime;
        this.minSendInterval = minSendInterval;
        this.verificationCodeExpireTime = verificationCodeExpireTime;
    }

    private BoundValueOperations<String, String> getVersion(String subject) {
        return redisTemplate.boundValueOps("version:" + subject);
    }

    private BoundValueOperations<String, String> getCode(String emailAddress) {
        return redisTemplate.boundValueOps("code:" + emailAddress);
    }

    private String nextCode() {
        return String.format("%06d", generator.nextInt(1000000));
    }

    public boolean validate(String subject, String keyId) {
        val compareResult = Objects.compare(keyId, getVersion(subject).get(), Comparators.nullsLow());
        return compareResult >= 0;
    }

    public String currentVersion(String subject) {
        val ops = getVersion(subject);
        val currentVersion = ops.get();
        return currentVersion == null ? "0" : currentVersion;
    }

    public void revoke(String subject) {
        val ops = getVersion(subject);
        ops.increment();
        ops.expire(jwtExpireTime);
    }

    @Nullable
    public String nextCodeFor(String emailAddress) {
        val code = nextCode();
        val ops = getCode(emailAddress);
        val now = Instant.now();
        val result = ops.setIfAbsent(code + now.toString());
        if (result == Boolean.TRUE) {
            ops.expire(verificationCodeExpireTime);
            return code;
        }
        val codeValue = Objects.requireNonNull(ops.get());
        val instantStr = codeValue.substring(6);
        val instant = Instant.parse(instantStr);
        val compareResult = now.compareTo(instant.plusSeconds(minSendInterval.getSeconds()));
        if (compareResult >= 0) {
            ops.set(code + now.toString());
            ops.expire(verificationCodeExpireTime);
            return code;
        }
        return null;
    }

    public boolean checkCodeAndConsume(String emailAddress, String code) {
        val ops = getCode(emailAddress);
        redisTemplate.watch(ops.getKey());
        val codeWithInstant = ops.get();
        if (codeWithInstant == null) return false;
        var result = Objects.equals(code, codeWithInstant.substring(0, 6));
        if (result) {
            redisTemplate.setEnableTransactionSupport(true);
            redisTemplate.multi();
            redisTemplate.delete(ops.getKey());
            val execResults = redisTemplate.exec();
            result = execResults.size() >= 1 && Objects.equals(Long.valueOf(execResults.get(0).toString()), 1L);
        }
        return result;
    }

}
