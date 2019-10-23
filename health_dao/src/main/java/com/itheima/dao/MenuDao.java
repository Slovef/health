package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Menu;
import com.itheima.pojo.Permission;

import java.util.List;

/**
 * 菜单管理
 */
public interface MenuDao {
    //通过用户名得到当前用户的所有菜单项存到list<Menu>中
    List<Menu> getAllMenuByUsername(String username);

    //新增菜单
    void addMenu(Menu menu) throws Exception;
    //根据前端输入的查询条件进行分页查询
    Page<Menu> findByQueryString(String queryString) throws Exception;
    //根据Id回显菜单
    Menu updateById(int id) throws Exception;
    //编辑菜单
    void modify(Menu menu) throws Exception;
    //删除前检查该权限是否绑定了角色
    int checkIsWithRole(int id) throws Exception;
    //如果没有绑定,就删除该权限
    void deleteMenu(int id) throws Exception;
    //查询所有的菜单返回
    List<Menu> findAllMenu();
}
