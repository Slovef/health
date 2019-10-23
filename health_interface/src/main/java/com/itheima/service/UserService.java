package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;

import java.util.List;
import java.util.Map;

/**
 * 用户管理(登录  权限  等)
 */
public interface UserService {

    /**
     * 通过用户登录的用户名查询用户
     * @param username
     * @return
     */
    User findByUsername(String username);

    /**
     * 查询所有的角色数据返回给新增用户时显示
     * @return
     */
    List<Role> findAllRole() throws Exception;

    /**
     * 添加用户信息
     * @param
     * @return
     */
    void addUser(User user, int[] roleIds) throws Exception;
    /**
     * 分页查询,填充页面
     * @param queryPageBean
     * @return
     */
    PageResult queryPage(QueryPageBean queryPageBean) throws Exception;

    /**
     * 编辑时回显当前用户信息和已关联的角色信息
     * @param id
     * @return
     */
    Map<String, Object> findById(int id) throws Exception;

    /**
     * 修改用户和中间表数据
     * @param
     * @return
     */
    void modify(User user, int[] roleIds) throws Exception;

    /**
     * 删除用户和中间表数据
     * @param id
     * @return
     */
    void delete(int id) throws Exception;

    /**
     * 用户登录后修改密码
     * @param map
     * @param map
     * @return
     */
    Boolean checkPassword(Map map) throws Exception;
}
