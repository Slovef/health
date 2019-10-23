package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 用户管理
 */
public interface UserDao {
    /**
     * 根据用户名查询user
     * @param username
     * @return
     */
    User findByUserName(String username);


    /**
     * 根据userId查询Set<Role>
     * @param id
     * @return
     */
    Set<Role> findByUserId(Integer id);





    /**
     * 添加用户
     *
     * @param
     * @return
     */
    Integer getMaxId() throws Exception;
    void addUser(User user) throws Exception;
    //新增用户/角色之间的中间表
    void addUser_Role(Map map) throws Exception;

    /**
     * 分页查询,填充页面
     *
     * @param queryPageBean
     * @return
     */
    Page<User> queryPage(String queryPageBean) throws Exception;

    /**
     * 编辑时回显当前用户信息和已关联的角色信息
     *
     * @param id
     * @return
     */
    User findUserById(int id) throws Exception;

    List<Integer> findRoleIdsById(int id) throws Exception;

    /**
     * 修改用户和中间表数据
     *
     * @param
     * @return
     */
    //修改用户数据
    void modifyUser(User user) throws Exception;

    //删除关联的中间表
    void deleteUser_Role(Integer id) throws Exception;



    /**
     * 删除用户和中间表数据
     *
     * @param id
     * @return
     */
    void deleteUser(int id) throws Exception;

    /**
     *根据用户名查询用户输入密码是否正确
     * @param map
     * @return
     */
    User checkPass(Map map) throws Exception;

    /**
     * 修改密码
     * @param map
     */
    void modifyPass(Map map) throws Exception;
}
