package com.example.service;

import com.example.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author baomidou
 * @since 2023-10-25
 */
public interface IMenuService extends IService<Menu> {

    List<Menu> findMenus(String name);
}
