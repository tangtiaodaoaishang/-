package com.example.controller;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.Result;
import com.example.pojo.Files;
import com.example.pojo.User;
import com.example.service.impl.UserServiceImpl;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

import com.example.service.ICourseService;
import com.example.entity.Course;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author baomidou
 * @since 2023-10-28
 */
@RestController
@RequestMapping("/course")
        public class CourseController {

@Resource
private ICourseService courseService;
@Resource
private UserServiceImpl u;

//新增和修改操作
@PostMapping("insert")
public Result save(@RequestBody Course course){
    return Result.success(courseService.saveOrUpdate(course));
        }
    @PostMapping("/studentCourse/{courseId}/{studentId}")
    public Result studentCourse(@PathVariable Integer courseId,@PathVariable Integer studentId){
        courseService.setStudentCourse(courseId,studentId);
        return Result.success();
    }
    //更新
    @PostMapping("update")
    public Result update(@RequestBody Course c){
        //新增和修改操作
        return Result.success(courseService.updateById(c));
    }
        //删除多条操作
        @PostMapping("/del/batch")
        public Result deleteBacth(@RequestBody List<Integer> ids) {
                return Result.success(courseService.removeByIds(ids));
        }
//删除操作
@DeleteMapping("/{id}")
public Result delete(@PathVariable Integer id) {
        return Result.success(courseService.removeById(id));
        }
//查询所有操作
@GetMapping
public Result findAll() {
        return Result.success(courseService.list());
        }
@GetMapping("/{id}")
public Result findOne(@PathVariable Integer id) {
        return Result.success(courseService.getById(id));
        }
        @GetMapping("/page")
        public Result findPage(@RequestParam String name,
                @RequestParam Integer pageNum,
                               @RequestParam Integer pageSize) {
      /* QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        Page<Course> page = courseService.page(new Page<>(pageNum, pageSize), queryWrapper);
        List<Course> records = page.getRecords();
        for (Course record : records) {
            User user = u.getById(record.getTeacherId());
           if(user != null) {
               record.setTeacher(user.getNickname());
           }

        }*/
                Page<Course> page = courseService.findPage(new Page<>(pageNum, pageSize), name);
                return Result.success(page);
        }


        }

