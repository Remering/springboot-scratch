package com.github.remering.scratch.springboot.mapper;

import com.github.remering.scratch.springboot.entity.CourseEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.UUID;

@Mapper
public interface CourseMapper {
    CourseEntity getByUUID(UUID uuid);

    int add(CourseEntity courseEntity);

    List<CourseEntity> getAll();

    int updateByUUID(CourseEntity courseEntity);
}
