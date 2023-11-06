package com.example.controller.dto;

import com.example.entity.Menu;
import lombok.Data;

import java.util.List;

//接收前端登录请求的参数
@Data
public class UserDTO {
    private Integer id;
    private String username;
    private String password;
    private String nickname;
//  private String avatarUrl;
    //token令牌
    private String token;
    private String role;
    private List<Menu> menus;


}
