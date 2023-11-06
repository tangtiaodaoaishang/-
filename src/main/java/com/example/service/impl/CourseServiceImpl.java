package com.example.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.Course;
import com.example.mapper.CourseMapper;
import com.example.service.ICourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2023-10-28
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {
    @Autowired
    private CourseMapper c;
    @Override
    public Page<Course> findPage(Page<Course> page, String name) {
        return c.findPage(page,name);
    }

    @Transactional //控制事务,保证原子性
    @Override
    public void setStudentCourse(Integer courseId, Integer studentId) {
        c.deleteStudentCourse(courseId,studentId);
        c.setStudentCourse(courseId,studentId);
    }
}
