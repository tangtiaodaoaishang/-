package com.example.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.Constants;
import com.example.common.Result;
import com.example.controller.dto.UserDTO;
import com.example.entity.Menu;
import com.example.exception.ServiceException;
import com.example.mapper.RoleMapper;
import com.example.mapper.RoleMenuMapper;
import com.example.mapper.UserMapper;
import com.example.pojo.User;
import com.example.service.IMenuService;
import com.example.service.IUserService;
import com.example.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper,User>  implements IUserService {
    //使用hutool工具类的日志
    private static final Log l = Log.get();
    @Autowired
    private RoleMapper r;
    @Autowired
    private RoleMenuMapper r1;
    @Resource
    private IMenuService i;
    @Resource
    private UserMapper u;

    public boolean saveUser(User u) {
        if (u.getId() == null) {
            return save(u);
        } else {
            return updateById(u);
        }
//       return saveOrUpdate(u);
    }

    //登录
    @Override
    public UserDTO login(UserDTO u) {
        User one = getUserInfo(u);
        if (one != null) {
            BeanUtil.copyProperties(one, u, true);
            //设置Token
            String token = TokenUtils.genToken(one.getId().toString(), one.getPassword());
            u.setToken(token);
            String role = one.getRole();
            //找出当前角色的菜单列表
            List<Menu> roleMenus = getRoleMenus(role);
            u.setMenus(roleMenus);
            return u;
        } else {
            throw new ServiceException(Constants.CODE_600, "用户名或密码错误");
        }
    }


    public User register(UserDTO u) {
        User one = getUserInfo(u);
        if (one == null) {
            one = new User();
            BeanUtil.copyProperties(u, one, true);
            save(one); //把copy完之后的用户对象存储到数据库
        } else {
            throw new ServiceException(Constants.CODE_600, "用户已存在");
        }

        return one;
    }

    @Override
    public Page<User> findPage(Page<User> page, String username, String email, String address) {
          return  u.findPage(page,username,email,address);

    }

    private User getUserInfo(UserDTO u) {
        QueryWrapper<User> q = new QueryWrapper<>();
        q.eq("username", u.getUsername());
        q.eq("password", u.getPassword());
          /*用该方式如果数据存在脏读(数据库表中有两条相同的数据),就会报错
        User one = getOne(q);
        return one!=null;*/

        //用以下方式避免数据库脏读
        //第一种方式
       /*
        List<User> list = list(q);
        return list.size()!=0;
        */
        //第二种方式
        User one;
        try {
            one = getOne(q);
            //catch语句捕捉数据库脏读异常
        } catch (Exception e) {
            //hutool工具类日志输出
            l.error(e);
            throw new ServiceException(Constants.CODE_500, "用户信息重复");
        }
        return one;
    }

    //获取当前角色的菜单列表
    private List<Menu> getRoleMenus(String roleFlag) {
        //查看role角色表中当前角色的id
        Integer roleId = r.selectByFlag(roleFlag);
        //当前角色的所有菜单集合
        List<Integer> menuIds = r1.selectByRoleId(roleId);
        //查出系统中的所有菜单(父/子菜单)
        List<Menu> menus = i.findMenus("");
        //new一个最后筛选完成之后数据的list集合
        List<Menu> roleMenus = new ArrayList<>();
        //筛选当前用户角色的菜单
        for (Menu m : menus) {
            if (menuIds.contains(m.getId())) {
                roleMenus.add(m);
            }
            List<Menu> children = m.getChildren();
            //removeIf:移除children里面不在menuIds集合中的元素
            children.removeIf(child -> !menuIds.contains(child.getId()));

        }
        //最终返回一个属于当前角色的父/子菜单的集合数据
        return roleMenus;
    }

    }




