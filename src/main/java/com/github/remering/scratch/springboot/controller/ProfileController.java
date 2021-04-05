package com.github.remering.scratch.springboot.controller;

import com.github.remering.scratch.springboot.bean.ResponseCode;
import com.github.remering.scratch.springboot.bean.ResponseMessage;
import com.github.remering.scratch.springboot.bean.AccountPrincipal;
import com.github.remering.scratch.springboot.bean.message.ProfileMessage;
import com.github.remering.scratch.springboot.message.profile.GetProfile;
import com.github.remering.scratch.springboot.message.profile.UpdateProfile;
import com.github.remering.scratch.springboot.service.AccountService;
import com.github.remering.scratch.springboot.service.AvatarService;
import com.github.remering.scratch.springboot.service.UserUploadService;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.github.remering.scratch.springboot.util.Codec.hexStr2Bytes;

@RestController
@AllArgsConstructor
@RequestMapping("/plarform/user")
public class ProfileController {

    private final AvatarService avatarService;

    private final AccountService accountService;

    private final UserUploadService userUploadService;

    @GetMapping("/getProfile")
    public GetProfile.Response getProfile(AccountPrincipal principal) {

        val avatarEntity = avatarService.getByAccountUUID(principal.getUuid());
        val avatarURI = avatarEntity == null ? null : userUploadService.getURI(avatarEntity.getUserUpload());
        val profileMessage = ProfileMessage.builder()
                .uuid(principal.getUuid())
                .username(principal.getUsername())
                .email(principal.getEmail())
                .role(principal.getRole())
                .avatarUrl(avatarURI)
                .build();

        return GetProfile.Response.builder()
                .code(ResponseCode.SUCCESS)
                .message(ResponseMessage.OK)
                .profile(profileMessage)
                .build();
    }

    @PostMapping("/updateProfile")
    public UpdateProfile.Response updateProfile(
            AccountPrincipal principal,
            @RequestPart(required = false) MultipartFile avatar,
            @RequestPart(required = false) String sha256
    ) {

        if (avatar == null || sha256 == null)
            return UpdateProfile.Response.builder()
                    .code(ResponseCode.INFO)
                    .message(ResponseMessage.NO_ACTION)
                    .build();

        val userUploadEntity = accountService.updateAvatar(principal.toAccountEntity(), avatar, hexStr2Bytes(sha256));

        if (userUploadEntity == null) {
            return UpdateProfile.Response.builder()
                    .code(ResponseCode.ERROR)
                    .message(ResponseMessage.RETRY_LATER)
                    .build();
        }

        val profileMessage = ProfileMessage.builder()
                .uuid(principal.getUuid())
                .username(principal.getUsername())
                .role(principal.getRole())
                .email(principal.getEmail())
                .avatarUrl(userUploadService.getURI(userUploadEntity))
                .build();

        return UpdateProfile.Response.builder()
                .code(ResponseCode.SUCCESS)
                .message(ResponseMessage.OK)
                .profile(profileMessage)
                .build();
    }
}
