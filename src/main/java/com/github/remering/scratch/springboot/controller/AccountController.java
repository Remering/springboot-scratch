package com.github.remering.scratch.springboot.controller;

import com.github.remering.scratch.springboot.bean.ResponseCode;
import com.github.remering.scratch.springboot.bean.ResponseMessage;
import com.github.remering.scratch.springboot.bean.AccountPrincipal;
import com.github.remering.scratch.springboot.entity.AccountEntity;
import com.github.remering.scratch.springboot.message.account.*;
import com.github.remering.scratch.springboot.service.AccountService;
import com.github.remering.scratch.springboot.service.JwtService;
import com.github.remering.scratch.springboot.service.MailService;
import com.github.remering.scratch.springboot.service.RedisService;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.UUID;

import static com.github.remering.scratch.springboot.util.Codec.hexStr2Bytes;

@RestController
@RequestMapping("/plarform/user")
@AllArgsConstructor
public class AccountController {

    private final RedisService redisService;
    private final JwtService jwtService;
    private final AccountService accountService;
    private final MailService mailService;

    @PostMapping("/register")
    public Register.Response register(@RequestBody Register.Request request) {
        request.setEmail(request.getEmail().toLowerCase());
        if (!Objects.equals(request.getPassword(), request.getPasswordConfirm())) {
            return Register.Response.builder()
                    .code(ResponseCode.ERROR)
                    .message(ResponseMessage.WRONG_PASSWORD_CONFIRM)
                    .build();
        }

        if (!redisService.checkCodeAndConsume(request.getEmail(), request.getVerificationCode())) {
            return Register.Response.builder()
                    .code(ResponseCode.ERROR)
                    .message(ResponseMessage.WRONG_VERIFICATION_CODE)
                    .build();
        }

        val account = AccountEntity.builder()
                .uuid(UUID.randomUUID())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(hexStr2Bytes(request.getPassword()))
                .role(request.getRole())
                .build();

        return accountService.register(account);
    }

    @PostMapping("/login")
    public Login.Response login(@RequestBody Login.Request request) {
        val password = hexStr2Bytes(request.getPassword());
        val account = accountService.login(request.getAccount(), password);

        return account == null
                ? Login.Response.builder()
                    .code(ResponseCode.ERROR)
                    .message(ResponseMessage.WRONG_EMAIL_OR_PASSWORD)
                    .build()
                : Login.Response.builder()
                    .token(jwtService.createToken(account))
                    .code(ResponseCode.SUCCESS)
                    .message(ResponseMessage.OK)
                    .build();
    }

    @GetMapping("/logout")
    public Logout.Response logout(AccountPrincipal principal) {
        redisService.revoke(principal.getUuid().toString());

        return Logout.Response.builder()
                .code(ResponseCode.SUCCESS)
                .message("操作成功")
                .build();
    }

    @PostMapping("/findPassword")
    public FindPassword.Response findPassword(@RequestBody FindPassword.Request request) {
        if (!Objects.equals(request.getNewPassword(), request.getNewPasswordConfirm())) {
            return FindPassword.Response.builder()
                    .code(ResponseCode.ERROR)
                    .message(ResponseMessage.WRONG_PASSWORD_CONFIRM)
                    .build();
        }

        if (!redisService.checkCodeAndConsume(request.getEmail(), request.getVerificationCode())) {
            return FindPassword.Response.builder()
                    .code(ResponseCode.ERROR)
                    .message(ResponseMessage.WRONG_VERIFICATION_CODE)
                    .build();
        }

        accountService.resetPassword(request.getEmail(), hexStr2Bytes(request.getNewPassword()));

        return FindPassword.Response.builder()
                .code(ResponseCode.SUCCESS)
                .message(ResponseMessage.OK)
                .build();
    }

    @PostMapping("/changePassword")
    public ChangePassword.Response changePassword(@RequestBody ChangePassword.Request request) {
        if (!Objects.equals(request.getNewPassword(), request.getNewPasswordConfirm())) {
            return ChangePassword.Response.builder()
                    .code(ResponseCode.ERROR)
                    .message(ResponseMessage.WRONG_PASSWORD_CONFIRM)
                    .build();
        }

        val result = accountService.changePassword(
                request.getEmail(),
                hexStr2Bytes(request.getOldPassword()),
                hexStr2Bytes(request.getNewPassword())
        );

        val responseCode = result ? ResponseCode.SUCCESS : ResponseCode.ERROR;
        val responseMessage = result ? ResponseMessage.OK : ResponseMessage.WRONG_EMAIL_OR_PASSWORD;

        return ChangePassword.Response.builder()
                .code(responseCode)
                .message(responseMessage)
                .build();
    }

    @PostMapping("/sendVerificationCode")
    public SendVerificationCode.Response sendVerificationCode(@RequestBody SendVerificationCode.Request request) {
        val result = mailService.sendVerificationCode(request.getEmail().toLowerCase());

        val responseCode = result ? ResponseCode.SUCCESS : ResponseCode.ERROR;
        val responseMessage = result
                ? ResponseMessage.VERIFICATION_CODE_SENT
                : ResponseMessage.VERIFICATION_CODE_REQUEST_TOO_FREQUENTLY;

        return SendVerificationCode.Response.builder()
                .code(responseCode)
                .message(responseMessage)
                .build();
    }

}
