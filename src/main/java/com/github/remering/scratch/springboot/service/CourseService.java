package com.github.remering.scratch.springboot.service;

import com.github.remering.scratch.springboot.bean.AccountPrincipal;
import com.github.remering.scratch.springboot.bean.ResponseCode;
import com.github.remering.scratch.springboot.bean.ResponseMessage;
import com.github.remering.scratch.springboot.bean.message.CourseMessage;
import com.github.remering.scratch.springboot.entity.CourseEntity;
import com.github.remering.scratch.springboot.entity.UserUploadEntity;
import com.github.remering.scratch.springboot.mapper.CourseMapper;
import com.github.remering.scratch.springboot.message.course.CreateCourse;
import com.github.remering.scratch.springboot.message.course.UpdateCourse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import lombok.var;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

import static com.github.remering.scratch.springboot.util.Codec.*;

@Slf4j
@Service
@AllArgsConstructor
public class CourseService {

    private final CourseMapper courseMapper;

    private final UserUploadService userUploadService;

    public CourseEntity getByUUID(UUID uuid) {
        return courseMapper.getByUUID(uuid);
    }

    public List<CourseEntity> getAll() {
        return courseMapper.getAll();
    }

    @Transactional
    public CreateCourse.Response createCourse(
            AccountPrincipal principal,
            String name,
            String introduction,
            MultipartFile file,
            MultipartFile picture,
            MultipartFile video,
            String pictureSha256,
            String videoSha256,
            String fileSha256
    ) {
        val fileUploadEntity = userUploadService.save(file, hexStr2Bytes(fileSha256));
        val pictureUploadEntity = userUploadService.save(picture, hexStr2Bytes(pictureSha256));
        val videoUploadEntity = userUploadService.save(video, hexStr2Bytes(videoSha256));
        if (fileUploadEntity == null || pictureUploadEntity == null || videoUploadEntity == null) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CreateCourse.Response.builder()
                    .code(ResponseCode.ERROR)
                    .message(ResponseMessage.RETRY_LATER)
                    .build();
        }

        val accountEntity = principal.toAccountEntity();

        val courseEntity = CourseEntity.builder()
                .uuid(UUID.randomUUID())
                .name(name)
                .introduction(introduction)
                .file(fileUploadEntity)
                .picture(pictureUploadEntity)
                .video(videoUploadEntity)
                .teacher(accountEntity)
                .build();

        val result = courseMapper.add(courseEntity);
        final ResponseCode responseCode;
        final String responseMessage;
        CourseMessage courseMessage = CourseMessage.builder()
                .uuid(courseEntity.getUuid())
                .name(courseEntity.getName())
                .introduction(courseEntity.getIntroduction())
                .teacher(principal.getUuid())
                .file(userUploadService.getURI(courseEntity.getFile()))
                .picture(userUploadService.getURI(courseEntity.getPicture()))
                .video(userUploadService.getURI(courseEntity.getVideo()))
                .build();

        switch (result) {
            case 1: {
                responseCode = ResponseCode.SUCCESS;
                responseMessage = ResponseMessage.OK;
                break;
            }
            case 0: {
                responseCode = ResponseCode.WARNING;
                responseMessage = ResponseMessage.NO_ACTION;
                break;
            }
            default: {
                log.error(
                        "create course with uuid {} returns {}, which is greater than 1",
                        courseEntity.getUuid(),
                        result
                );
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                responseCode = ResponseCode.ERROR;
                responseMessage = ResponseMessage.RETRY_LATER;
                courseMessage = null;
            }
        }

        return CreateCourse.Response.builder()
                .code(responseCode)
                .message(responseMessage)
                .course(courseMessage)
                .build();
    }


    @Transactional
    public UpdateCourse.Response updateCourse(
            AccountPrincipal principal,
            CourseEntity originCourseEntity,
            String name,
            String introduction,
            MultipartFile file,
            MultipartFile picture,
            MultipartFile video,
            String pictureSha256,
            String videoSha256,
            String fileSha256
    ) {
        UserUploadEntity fileUploadEntity = null;
        UserUploadEntity pictureUploadEntity = null;
        UserUploadEntity videoUploadEntity = null;
        if (file != null && !file.isEmpty() && StringUtils.hasLength(fileSha256)) {
            fileUploadEntity = userUploadService.save(file, hexStr2Bytes(fileSha256));
        }

        if (picture != null && !picture.isEmpty() && StringUtils.hasLength(pictureSha256)) {
            pictureUploadEntity = userUploadService.save(picture, hexStr2Bytes(pictureSha256));

        }

        if (video != null && !video.isEmpty() && StringUtils.hasLength(videoSha256)) {
            videoUploadEntity = userUploadService.save(video, hexStr2Bytes(videoSha256));
        }

        var courseEntity = CourseEntity.builder()
                .uuid(originCourseEntity.getUuid())
                .name(name)
                .introduction(introduction)
                .file(fileUploadEntity)
                .picture(pictureUploadEntity)
                .video(videoUploadEntity)
                .build();


        val result = courseMapper.updateByUUID(courseEntity);

        courseEntity = CourseEntity.builder()
                .uuid(originCourseEntity.getUuid())
                .name(name == null ? originCourseEntity.getName() : name)
                .teacher(originCourseEntity.getTeacher())
                .introduction(introduction == null ? originCourseEntity.getIntroduction() : introduction)
                .file(fileUploadEntity == null ? originCourseEntity.getFile() : fileUploadEntity)
                .picture(pictureUploadEntity == null ? originCourseEntity.getPicture() : pictureUploadEntity)
                .video(videoUploadEntity == null ? originCourseEntity.getVideo() : videoUploadEntity)
                .build();

        final ResponseCode responseCode;
        final String responseMessage;
        CourseMessage courseMessage = CourseMessage.builder()
                .uuid(courseEntity.getUuid())
                .name(courseEntity.getName())
                .introduction(courseEntity.getIntroduction())
                .teacher(principal.getUuid())
                .file(userUploadService.getURI(courseEntity.getFile()))
                .picture(userUploadService.getURI(courseEntity.getPicture()))
                .video(userUploadService.getURI(courseEntity.getVideo()))
                .build();

        switch (result) {
            case 1: {
                responseCode = ResponseCode.SUCCESS;
                responseMessage = ResponseMessage.OK;
                break;
            }
            case 0: {
                responseCode = ResponseCode.WARNING;
                responseMessage = ResponseMessage.NO_ACTION;
                break;
            }
            default: {
                log.error(
                        "update course with uuid {} returns {}, which is greater than 1",
                        courseEntity.getUuid(),
                        result
                );
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                responseCode = ResponseCode.ERROR;
                responseMessage = ResponseMessage.RETRY_LATER;
                courseMessage = null;
            }
        }

        return UpdateCourse.Response.builder()
                .code(responseCode)
                .message(responseMessage)
                .course(courseMessage)
                .build();
    }
}
