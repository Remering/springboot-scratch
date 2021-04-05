package com.github.remering.scratch.springboot.service;

import lombok.val;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private final JavaMailSender sender;
    private final String emailFrom;
    private final RedisService redisService;

    public MailService(
            JavaMailSender sender,
            @Qualifier("verificationCodeEmailFrom")
            String emailFrom,
            RedisService redisService
    ) {
        this.sender = sender;
        this.emailFrom = emailFrom;
        this.redisService = redisService;
    }

    public boolean sendVerificationCode(String emailAddress) {
        val code = redisService.nextCodeFor(emailAddress);
        if (code == null) return false;
        val message = new SimpleMailMessage();
        message.setFrom(emailFrom);
        message.setTo(emailAddress);
        message.setSubject("注册验证码");
        message.setText(String.format("您的验证码为%s", code));
        sender.send(message);
        return true;
    }
}
