package com.github.remering.scratch.springboot.mapper;

import com.github.remering.scratch.springboot.entity.QuestionEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.UUID;

@Mapper
public interface QuestionMapper {


    QuestionEntity getByUUID(UUID uuid);

    List<QuestionEntity> getAllByCourseUUID(UUID courseUUID);

    int add(@Param("entity") QuestionEntity entity, @Param("courseUUID") UUID courseUUID);

    int updateByUUID(QuestionEntity entity);

    int deleteByUUID(UUID uuid);

}
