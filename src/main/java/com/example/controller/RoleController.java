package com.example.controller;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.Result;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

import com.example.service.IRoleService;
import com.example.entity.Role;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author baomidou
 * @since 2023-10-25
 */
@RestController
@RequestMapping("/role")
public class RoleController {

@Resource
private IRoleService roleService;

//新增和修改操作
@PostMapping("insert")
public Result save(@RequestBody Role role){

        return Result.success(roleService.saveOrUpdate(role));
        }
        //删除多条操作
        @PostMapping("/del/batch")
        public Result deleteBacth(@RequestBody List<Integer> ids) {
                return Result.success(roleService.removeByIds(ids));
        }
//删除操作
@DeleteMapping("/{id}")
public Result delete(@PathVariable Integer id) {
        return Result.success(roleService.removeById(id));
        }
//查询所有操作
@GetMapping
public Result findAll() {
        return Result.success(roleService.list());
        }
@GetMapping("/{id}")
public Result findOne(@PathVariable Integer id) {
        return Result.success(roleService.getById(id));
        }

@GetMapping("/page")
public Result findPage(@RequestParam String name,
        @RequestParam Integer pageNum,
       @RequestParam Integer pageSize) {
        QueryWrapper<Role> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.like("name",name);
        objectQueryWrapper.orderByDesc("id");
        Page<Role> page = roleService.page(new Page<>(pageNum, pageSize), objectQueryWrapper);
        return Result.success(page);

        }
        //绑定角色和菜单的关系
          /*   roleId:角色数据id
               menuIds:菜单数据id*/
        @PostMapping("/roleMenu/{roleId}")
        public Result roleMenu(@PathVariable Integer roleId,@RequestBody List<Integer> menuIds){
                roleService.setRoleMenu(roleId,menuIds);
                return Result.success();
        }
        @GetMapping("/roleMenu/{roleId}")
        public Result getRoleMenu(@PathVariable Integer roleId){
                return Result.success(roleService.getRoleMenu(roleId));
        }



}


