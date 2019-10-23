package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Role;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 用户角色管理
 */
public interface RoleDao {

    /**
     * 根据userId查询Set<Role>
     * @param id
     * @return
     */
    Set<Role> findByUserId(Integer id);





    /**
     * 添加角色
     *
     * @param
     * @return
     */
    Integer getMaxId(Role role) throws Exception;
    void addRole(Role role) throws Exception;
    //新增菜单/权限和角色之间的中间表
    void addMenu_Role(Map<String, Integer> map) throws Exception;
    void addPermission_Role(Map<String, Integer> map) throws Exception;

    /**
     * 分页查询,填充页面
     *
     * @param queryPageBean
     * @return
     */
    Page<Role> queryPage(String queryPageBean) throws Exception;

    /**
     * 编辑时回显已关联的菜单和权限
     *
     * @param id
     * @return
     */
    Role findRoleById(int id) throws Exception;

    Integer[] findMenuIdsById(int id) throws Exception;
    Integer[] findPermissionIdsById(int id) throws Exception;

    /**
     * 修改检查组合中间表数据
     *
     * @param
     * @return
     */
    //修改角色数据
    void modifyRole(Role role) throws Exception;

    //删除关联的中间表
    void deleteRole_Menu(Integer id) throws Exception;
    void deleteRole_Permission(Integer id) throws Exception;



    /**
     * 删除角色和中间表数据
     *
     * @param id
     * @return
     */
    void deleteRole(int id) throws Exception;

    /**
     * 查询所有的角色信息
     * @return
     */
    List<Role> findAllrole() throws Exception;
}
