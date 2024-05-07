package com.example.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface UserMapper  extends BaseMapper<User> {
    Page<User> findPage(Page<User> page, @Param("username") String username, @Param("email") String email, @Param("address") String address);

    /*@Select("select * from sys_user")
    List<User> findAll();

    @Insert("insert into sys_user(username,password,nickname,email,phone,address) values(#{username}," +
            "#{password},#{nickname},#{email},#{phone},#{address});")
    int insert(User u);
   *//* @Update("update sys_user set username=#{username},password=#{password},nickname=#{nickname},email=#{email}," +
            "phone=#{phone},address=#{address} where id=#{id}")*//*
    int update(User u1);
    @Delete("delete from sys_user where id=#{id}")
    int deleteById(@Param("id") Integer id);
    @Select("select * from sys_user where username like concat('%',#{username},'%') limit #{pageNum},#{pageSize}")
    List<User> selectPage(Integer pageNum, Integer pageSize,String username);
    @Select("select count(*) from sys_user where username like concat('%',#{username},'%')")
    int selectTotal(String username);*/
}

