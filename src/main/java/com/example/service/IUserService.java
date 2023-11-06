package com.example.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.common.Result;
import com.example.controller.dto.UserDTO;
import com.example.pojo.User;

public interface IUserService extends IService<User> {
   //登录方法
   UserDTO login(UserDTO u);
//    int save(User u);

   //注册方法
   User register(UserDTO u);


    Page<User> findPage(Page<User> tPage, String username, String email, String address);
}
