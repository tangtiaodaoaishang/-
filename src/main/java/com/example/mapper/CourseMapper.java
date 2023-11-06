package com.example.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.Course;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author baomidou
 * @since 2023-10-28
 */
@Mapper
public interface CourseMapper extends BaseMapper<Course> {
    Page<Course> findPage(Page<Course> page, @Param("name") String name);
    void deleteStudentCourse(@Param("courseId") Integer courseId, @Param("studentId") Integer studentId);
    void setStudentCourse(@Param("courseId") Integer courseId, @Param("studentId") Integer studentId);
}
