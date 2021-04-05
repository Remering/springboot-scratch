package com.github.remering.scratch.springboot.service;

import com.github.remering.scratch.springboot.entity.QuestionEntity;
import com.github.remering.scratch.springboot.mapper.QuestionMapper;
import lombok.val;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class QuestionService {

    private final QuestionMapper questionMapper;

    public QuestionService(QuestionMapper questionMapper) {
        this.questionMapper = questionMapper;
    }

    public QuestionEntity getByUUID(UUID uuid) {
        return questionMapper.getByUUID(uuid);
    }

    @Transactional
    public boolean addQuestion(QuestionEntity entity, UUID courseUUID) {
        val result = questionMapper.add(entity, courseUUID);
        if (result == 1) return true;
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        return false;
    }

    public List<QuestionEntity> getAllByCourseUUID(UUID courseUUID) {
        return questionMapper.getAllByCourseUUID(courseUUID);
    }

    @Transactional
    public boolean delete(QuestionEntity entity) {
        val result = questionMapper.deleteByUUID(entity.getUuid());
        if (result != 1) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
        return true;
    }

    @Nullable
    @Transactional
    public QuestionEntity answerQuestion(UUID questionUUID, String newAnswer, Instant answerAt) {
        val entity = QuestionEntity.builder()
                .uuid(questionUUID)
                .answer(newAnswer)
                .answerAt(answerAt)
                .build();
        val result = questionMapper.updateByUUID(entity);

        if (result != 1) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return null;
        }

        return questionMapper.getByUUID(questionUUID);
    }

    @Nullable
    @Transactional
    public QuestionEntity modifyText(UUID questionUUID, String newText, Instant issueAt) {
        val entity = QuestionEntity.builder()
                .uuid(questionUUID)
                .issueAt(issueAt)
                .text(newText)
                .build();

        val result = questionMapper.updateByUUID(entity);

        if (result != 1) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return null;
        }

        return questionMapper.getByUUID(questionUUID);
    }
}
