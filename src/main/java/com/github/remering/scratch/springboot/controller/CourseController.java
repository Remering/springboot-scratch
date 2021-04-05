package com.github.remering.scratch.springboot.controller;

import com.github.remering.scratch.springboot.bean.ResponseCode;
import com.github.remering.scratch.springboot.bean.ResponseMessage;
import com.github.remering.scratch.springboot.bean.Role;
import com.github.remering.scratch.springboot.bean.AccountPrincipal;
import com.github.remering.scratch.springboot.bean.message.CourseMessage;
import com.github.remering.scratch.springboot.bean.message.TeacherCourseMessage;
import com.github.remering.scratch.springboot.entity.CourseEntity;
import com.github.remering.scratch.springboot.message.course.CreateCourse;
import com.github.remering.scratch.springboot.message.course.GetCourses;
import com.github.remering.scratch.springboot.message.course.UpdateCourse;
import com.github.remering.scratch.springboot.service.AvatarService;
import com.github.remering.scratch.springboot.service.CourseService;
import com.github.remering.scratch.springboot.service.UserUploadService;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/plarform/teacher")
public class CourseController {

    private final CourseService courseService;

    private final UserUploadService userUploadService;

    private final AvatarService avatarService;

    @PostMapping(value = "/createCourse", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CreateCourse.Response createCourse(
            AccountPrincipal principal,
            @RequestParam String name,
            @RequestParam String introduction,
            @RequestPart MultipartFile file,
            @RequestPart MultipartFile picture,
            @RequestPart MultipartFile video,
            @RequestParam String pictureSha256,
            @RequestParam String videoSha256,
            @RequestParam String fileSha256
    ) {

        if (principal.getRole().ordinal() < Role.TEACHER.ordinal()) {
            return CreateCourse.Response.builder()
                    .code(ResponseCode.ERROR)
                    .message(ResponseMessage.INSUFFICIENT_PERMISSION)
                    .build();
        }

        return courseService.createCourse(
                principal,
                name,
                introduction,
                file,
                picture,
                video,
                pictureSha256,
                videoSha256,
                fileSha256
        );
    }

    @PostMapping(value = "/updateCourse", consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE,
            MediaType.APPLICATION_OCTET_STREAM_VALUE
    })
    public UpdateCourse.Response updateCourse(
            AccountPrincipal principal,
            @RequestParam("uuid") UUID courseUUID,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String introduction,
            @RequestPart(required = false) MultipartFile file,
            @RequestPart(required = false) MultipartFile picture,
            @RequestPart(required = false) MultipartFile video,
            @RequestParam(required = false) String pictureSha256,
            @RequestParam(required = false) String videoSha256,
            @RequestParam(required = false) String fileSha256
    ) {

        val courseEntity = courseService.getByUUID(courseUUID);

        if (!Objects.equals(courseEntity.getTeacher().getUuid(), principal.getUuid()) ) {
            return UpdateCourse.Response.builder()
                    .code(ResponseCode.ERROR)
                    .message(ResponseMessage.INSUFFICIENT_PERMISSION)
                    .build();
        }

        return courseService.updateCourse(
                principal,
                courseEntity,
                name,
                introduction,
                file,
                picture,
                video,
                pictureSha256,
                videoSha256,
                fileSha256
        );
    }

    @GetMapping("/getCourses")
    public GetCourses.Response getCourses() {
        
        val courseList = courseService.getAll();

        val courseMap = new HashMap<UUID, TeacherCourseMessage>();

        for (CourseEntity courseEntity : courseList) {
            courseMap.put(courseEntity.getUuid(),
                    TeacherCourseMessage.builder()
                            .uuid(courseEntity.getUuid())
                            .name(courseEntity.getName())
                            .teacher(avatarService.attachAvatar(courseEntity.getTeacher()))
                            .introduction(courseEntity.getIntroduction())
                            .file(userUploadService.getURI(courseEntity.getFile()))
                            .picture(userUploadService.getURI(courseEntity.getPicture()))
                            .video(userUploadService.getURI(courseEntity.getVideo()))
                            .build()
            );
        }

        return GetCourses.Response.builder()
                .code(ResponseCode.SUCCESS)
                .message(ResponseMessage.OK)
                .courses(courseMap)
                .build();
    }
}
