package com.example.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.Menu;
import com.example.entity.Role;
import com.example.entity.RoleMenu;
import com.example.mapper.RoleMapper;
import com.example.mapper.RoleMenuMapper;
import com.example.service.IMenuService;
import com.example.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2023-10-25
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {
@Autowired
private RoleMenuMapper r;
  @Autowired
  private IMenuService i;
    @Transactional //事务,保证所有任务都能执行到
    @Override
    public void setRoleMenu(Integer roleId, List<Integer> menuIds) {
/*      QueryWrapper<RoleMenu> q=new QueryWrapper<>();
        q.eq("role_id",roleId);
        r.delete(q);*/

        //先删除当前角色id所有的绑定关系
         r.deleteByRoleId(roleId);


        List<Integer> menuIdsCopy= CollUtil.newArrayList(menuIds);
        //再把前端传过来的菜单id数组绑定到当前这个角色id上去
        for(Integer menuId:menuIds){
            Menu byId = i.getById(menuId);
            if(byId.getPid()!=null&&!menuIdsCopy.contains(byId.getPid())){ //是二级菜单并且传过来的menuIds数组里面没有它的父级菜单id
                //那么就要补上父级菜单的id
                RoleMenu menu = new RoleMenu();
                menu.setRoleId(roleId);
                menu.setMenuId(byId.getPid());
                r.insert(menu);
                menuIdsCopy.add(byId.getPid());

            }
            RoleMenu menu = new RoleMenu();
            menu.setRoleId(roleId);
            menu.setMenuId(menuId);
            r.insert(menu);
        }

    }

    @Override
    public List<Integer> getRoleMenu(Integer roleId) {
        return r.selectByRoleId(roleId);
    }
}
