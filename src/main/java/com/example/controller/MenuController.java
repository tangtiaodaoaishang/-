package com.example.controller;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.Constants;
import com.example.common.Result;
import com.example.mapper.Dict;
import com.example.mapper.DictMapper;
import com.example.service.impl.MenuServiceImpl;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import com.example.service.IMenuService;
import com.example.entity.Menu;

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
@RequestMapping("/menu")
public class MenuController {
    
@Resource
private IMenuService menuService;
@Resource
private MenuServiceImpl s;
@Resource
private DictMapper d;

//新增和修改操作
@PostMapping("/insert")
public Result save(@RequestBody Menu menu){
//        return Result.success(menuService.saveOrUpdate(menu));
        return Result.success(s.saveMenu(menu));
        }
@GetMapping("/ids")
public Result findAllIds(){
        return Result.success(menuService.list().stream().map(Menu::getId));
}
        //删除多条操作
        @PostMapping("/del/batch")
        public Result deleteBacth(@RequestBody List<Integer> ids) {
                return Result.success(menuService.removeByIds(ids));
        }
//删除单条操作
@DeleteMapping("/{id}")
public Result delete(@PathVariable Integer id) {
        return Result.success(menuService.removeById(id));
        }
//查询所有操作
@GetMapping
public Result findAll(@RequestParam(defaultValue = "") String name) {
        //返回一级菜单
        return Result.success(menuService.findMenus(name));
        }
@GetMapping("/{id}")
public Result findOne(@PathVariable Integer id) {
        return Result.success(menuService.getById(id));
        }

@GetMapping("/page")
public Result findPage(@RequestParam String name,
        @RequestParam Integer pageNum,
       @RequestParam Integer pageSize) {
        QueryWrapper<Menu> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.like("name",name);
        objectQueryWrapper.orderByDesc("id");
        return Result.success(menuService.page(new Page<>(pageNum, pageSize),objectQueryWrapper));

        }

        @GetMapping("/icon")
        public Result getIcons() {
        QueryWrapper<Dict> q=new QueryWrapper<Dict>();
        q.eq("type", Constants.DICT_TYPE_ICON);
                return Result.success(d.selectList(q));
        }

        }

