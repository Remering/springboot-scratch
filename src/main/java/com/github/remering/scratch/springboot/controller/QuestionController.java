package com.github.remering.scratch.springboot.controller;

import com.github.remering.scratch.springboot.bean.AccountPrincipal;
import com.github.remering.scratch.springboot.bean.ResponseCode;
import com.github.remering.scratch.springboot.bean.ResponseMessage;
import com.github.remering.scratch.springboot.bean.message.QuestionMessage;
import com.github.remering.scratch.springboot.entity.QuestionEntity;
import com.github.remering.scratch.springboot.message.question.*;
import com.github.remering.scratch.springboot.service.AvatarService;
import com.github.remering.scratch.springboot.service.QuestionService;
import com.github.remering.scratch.springboot.service.UserUploadService;
import lombok.AllArgsConstructor;
import lombok.val;
import lombok.var;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/plarform/question")
public class QuestionController {

    private final QuestionService questionService;

    private final AvatarService avatarService;

    private final UserUploadService userUploadService;



    @PostMapping("/add")
    public AddQuestion.Response addQuestion(AccountPrincipal principal, @RequestBody AddQuestion.Request request) {

        val accountEntity = principal.toAccountEntity();

        val questionEntity = QuestionEntity.builder()
                .issuer(accountEntity)
                .issueAt(request.getIssueAt())
                .text(request.getText())
                .build();

        val result = questionService.addQuestion(questionEntity, request.getCourse());

        val responseCode = result ? ResponseCode.SUCCESS : ResponseCode.ERROR;

        val responseMessage = result ? ResponseMessage.OK : ResponseMessage.RETRY_LATER;

        val questionMessage = !result ? null : QuestionMessage.from(questionEntity, avatarService.attachAvatar(questionEntity.getIssuer()));

        questionMessage.setCourse(request.getCourse());

        return AddQuestion.Response.builder()
                .code(responseCode)
                .message(responseMessage)
                .question(questionMessage)
                .build();
    }

    @PostMapping("/answer")
    public AnswerQuestion.Response answerQuestion(AccountPrincipal principal, @RequestBody AnswerQuestion.Request request) {

        val questionEntity = questionService.answerQuestion(
                request.getUuid(),
                request.getAnswer(),
                request.getAnswerAt()
        );

        val responseCode = questionEntity != null ? ResponseCode.SUCCESS : ResponseCode.ERROR;

        val responseMessage = questionEntity != null ? ResponseMessage.OK : ResponseMessage.RETRY_LATER;

        return AnswerQuestion.Response.builder()
                .code(responseCode)
                .message(responseMessage)
                .question(QuestionMessage.from(questionEntity, avatarService.attachAvatar(questionEntity.getIssuer())))
                .build();
    }

    @PostMapping("/delete")
    public DeleteQuestion.Response deleteQuestion(AccountPrincipal principal, @RequestBody DeleteQuestion.Request request) {
        val questionEntity = questionService.getByUUID(request.getUuid());
        if (questionEntity == null) {
            return DeleteQuestion.Response.builder()
                    .code(ResponseCode.ERROR)
                    .message(ResponseMessage.NO_SUCH_QUESTION)
                    .build();
        }

        var hasPermission = Objects.equals(principal.getUuid(), questionEntity.getIssuer().getUuid());
        hasPermission = hasPermission || Objects.equals(principal.getUuid(), questionEntity.getCourse().getTeacher().getUuid());
        if (!hasPermission) {
            return DeleteQuestion.Response.builder()
                    .code(ResponseCode.ERROR)
                    .message(ResponseMessage.INSUFFICIENT_PERMISSION)
                    .build();
        }
        val result = questionService.delete(questionEntity);

        val responseCode = result ? ResponseCode.SUCCESS : ResponseCode.ERROR;

        val responseMessage = result ? ResponseMessage.OK : ResponseMessage.RETRY_LATER;

        return DeleteQuestion.Response.builder()
                .code(responseCode)
                .message(responseMessage)
                .build();
    }

    @PostMapping("/get")
    public GetQuestion.Response getQuestions(@RequestBody GetQuestion.Request request) {
        val questionList = questionService.getAllByCourseUUID(request.getCourse());
        val questionMap = new HashMap<UUID, QuestionMessage>();
        for (QuestionEntity question : questionList) {

            questionMap.put(
                    question.getUuid(),
                    QuestionMessage.from(question, avatarService.attachAvatar(question.getIssuer()))
            );
        }
        return GetQuestion.Response.builder()
                .code(ResponseCode.SUCCESS)
                .message(ResponseMessage.OK)
                .questions(questionMap)
                .build();
    }

    @PostMapping("/modifyText")
    public ModifyQuestion.Response modifyText(AccountPrincipal principal, @RequestBody ModifyQuestion.Request request) {
        val questionEntity = questionService.modifyText(
                request.getUuid(),
                request.getText(),
                request.getIssueAt()
        );

        val responseCode = questionEntity != null ? ResponseCode.SUCCESS : ResponseCode.ERROR;

        val responseMessage = questionEntity != null ? ResponseMessage.OK : ResponseMessage.RETRY_LATER;


        return ModifyQuestion.Response.builder()
                .code(responseCode)
                .question(QuestionMessage.from(questionEntity, avatarService.attachAvatar(questionEntity.getIssuer())))
                .message(responseMessage)
                .build();
    }

}
