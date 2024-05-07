package com.example.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.entity.Course;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "sys_user") //告诉mybatis-plus我的数据库表名叫啥
@ToString
public class User {
    @TableId(value = "id") //如果pojo类主键的字段名和数据库表中的主键字段名不一致时(或者实现id主键的自增策略),可以使用@TableId告诉mybatis-plus这个字段是表的主键id
    private Integer id;
    private String username;
    @JsonIgnore //忽略该字段,即前端数据不会显示该注解指定的字段
    private String password;
    private String nickname;
    private String email;
    private String phone;
    private String address;
    private Date createTime;
//   private String avatarUrl;
@ApiModelProperty("角色")
    private String role;
    //老师课程信息属性
    @TableField(exist = false)
   private List<Course> courses;
    //学生课程信息属性
    @TableField(exist = false)
    private List<Course> stuCourses;
    //学生信息属性
    @TableField(exist = false)
    private List<User> stus;


}
