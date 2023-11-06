package com.example.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.Quarter;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.example.common.Constants;
import com.example.common.Result;
import com.example.config.AuthAccess;
import com.example.mapper.FileMapper;
import com.example.pojo.Files;
import com.example.pojo.User;
import com.example.service.IUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/echarts")
public class EchartsController {
    @Autowired
    private IUserService a;
    @Autowired
    private FileMapper f;
    @Autowired
    private StringRedisTemplate r;


    //查询所有的File表中数据
    @AuthAccess
    @GetMapping("/front/all")
    //启动springboot缓存
//    @Cacheable(value = "files",key="'targeClass'+'methodName'")
//    @Cacheable(value = "files",key="'frontAll'")
    public Result frontAll(){
        //1.从缓存中获取数据
        String jsonStr = r.opsForValue().get(Constants.FILES_KEY);
        List<Files> files;
        if(StrUtil.isBlank(jsonStr)){ //2.缓存取出来的json数据是空的
            //3.从数据库取出数据后
             files = f.selectList(null);
             //4.将数据缓存到redis中
             r.opsForValue().set(Constants.FILES_KEY,JSONUtil.toJsonStr(files));
        }else{
            //5.从redis缓存中获取数据
            files = JSONUtil.toBean(jsonStr, new TypeReference<List<Files>>() {
            }, true);
        }

        return Result.success(files);
    }
    @GetMapping("/example")
    public Result get(){
        Map<String,Object> m=new HashMap<>();
        m.put("x", CollUtil.newArrayList("类别1", "类别2", "类别3", "类别4", "类别5"));
        m.put("y",CollUtil.newArrayList(20, 50, 30, 80, 70));
        return Result.success(m);
    }
    @GetMapping("/members")
    public  Result members(){
        List<User> list = a.list();
        int q1=0; //第一季度
        int q2=0; //第二季度
        int q3=0; //第三季度
        int q4=0; //第四季度
        for (User u:list){
            Date createTime = u.getCreateTime();
            //hutool中DateUtil类的quarterEnum方法:获得指定日期所属季度
            Quarter quarter = DateUtil.quarterEnum(createTime);
            switch (quarter){
                case Q1:
                    q1+=1;
                    break;
                case Q2:
                    q2+=1;
                    break;
                case Q3:
                    q3+=1;
                    break;
                case Q4:
                    q4+=1;
                    break;
                default:
                    break;
            }

        }
        return  Result.success(CollUtil.newArrayList(q1,q2,q3,q4));

    }
}
