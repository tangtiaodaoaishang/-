package com.example.controller;



import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.Result;

import com.example.utils.TokenUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

import com.example.service.IArticleService;
import com.example.entity.Article;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author baomidou
 * @since 2023-11-01
 */
@RestController
@RequestMapping("/article")
        public class ArticleController {
    
@Resource
private IArticleService articleService;

//新增和修改操作
@PostMapping("insert")
public Result save(@RequestBody Article article){
        //新增
          if(article.getId()==null){
                  //DateUtil.now()最新的时间
              article.setTime(DateUtil.now());
              article.setUser(TokenUtils.getCurrentUser().getNickname());
          }
        return Result.success(articleService.saveOrUpdate(article));
        }

//删除操作
@DeleteMapping("/{id}")
public Result delete(@PathVariable Integer id) {
        return Result.success(articleService.removeById(id));
        }
//删除操作
@PostMapping("/del/batch")
public Result delete(@RequestBody List<Integer> ids) {
        return Result.success(articleService.removeByIds(ids));
        }
//查询所有操作
@GetMapping
public Result findAll() {
        return Result.success(articleService.list());
        }
@GetMapping("/{id}")
public Result findOne(@PathVariable Integer id) {
        return Result.success(articleService.getById(id));
        }

@GetMapping("/page")
public Result findPage(@RequestParam String name,
        @RequestParam Integer pageNum,
        @RequestParam Integer pageSize) {
        QueryWrapper<Article> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.orderByDesc("id");
        if(StrUtil.isNotBlank(name)){
                objectQueryWrapper.like("name",name);
        }
        return Result.success(articleService.page(new Page<>(pageNum, pageSize),objectQueryWrapper));

        }

        }

