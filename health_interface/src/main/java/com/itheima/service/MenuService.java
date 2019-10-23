package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.pojo.Menu;
import com.itheima.pojo.Permission;

import java.util.List;
import java.util.Map;

/**
 * 菜单管理
 */
public interface MenuService {

    //根据用户登录的用户名查询该用户下的所有菜单项
    List<Map> getUserMenu(String username);


    /**
     * 菜单新增
     * @param menu
     */
    void add(Menu menu) throws Exception;

    /**
     * 检查项分页
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString) throws Exception;

    /**
     * 根据Id回显菜单
     * @param id
     */
    Menu updateById(int id) throws Exception;

    /**
     * 编辑菜单
     * @param menu
     */
    void modify(Menu menu) throws Exception;

    /**
     * 删除菜单
     * @param id
     */
    Boolean delete(int id) throws Exception;


}
