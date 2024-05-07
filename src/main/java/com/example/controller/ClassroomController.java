package com.example.controller;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.Constants;
import com.example.common.Result;
import com.example.mapper.ClassroomMapper;
import com.example.mapper.Dict;
import com.example.mapper.DictMapper;
import com.example.pojo.Files;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

import com.example.service.IClassroomService;
import com.example.entity.Classroom;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author baomidou
 * @since 2023-11-24
 */
@RestController
@RequestMapping("/classroom")
        public class ClassroomController {

@Resource
private IClassroomService classroomService;
@Resource
private ClassroomMapper f;
@Resource
private DictMapper d;

//新增和修改操作
@PostMapping("insert")
public Result save(@RequestBody Classroom classroom){

        return Result.success(classroomService.saveOrUpdate(classroom));
        }
        //更新
        @PostMapping("update")
        //清除缓存
        public Result update(@RequestBody Classroom r){
                //新增和修改操作
                f.updateById(r);
                return Result.success();
//        return Result.success(f.selectList(null));
        }
//删除操作
@DeleteMapping("/{id}")
public Result delete(@PathVariable Integer id) {
        return Result.success(classroomService.removeById(id));
        }
//删除操作
@PostMapping("/del/batch")
public Result delete(@RequestBody List<Integer> ids) {
        return Result.success(classroomService.removeByIds(ids));
        }
//查询所有操作
@GetMapping
public Result findAll() {
        return Result.success(classroomService.list());
        }
@GetMapping("/{id}")
public Result findOne(@PathVariable Integer id) {
        return Result.success(classroomService.getById(id));
        }

@GetMapping("/page")
public Result findPage(@RequestParam Integer pageNum,
@RequestParam Integer pageSize,@RequestParam(defaultValue = "") String name) {
        QueryWrapper<Classroom> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.like("name",name);
        objectQueryWrapper.orderByDesc("id");
        return Result.success(classroomService.page(new Page<>(pageNum, pageSize),objectQueryWrapper));

        }

        @GetMapping("/icon")
        public Result getIcons() {
                QueryWrapper<Dict> q=new QueryWrapper<Dict>();
                q.eq("type", Constants.DICT_TYPE_ICON);
                return Result.success(d.selectList(q));
        }

        }

