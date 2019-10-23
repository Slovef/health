package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Permission;

import java.util.List;
import java.util.Set;

/**
 * 权限管理
 */
public interface PermissionDao {
    /**
     * 根据roleid查询Permission
     * @param id
     * @return
     */
    Set<Permission> findPermissionByRoleId(Integer id);
    //新增权限
    void addPermission(Permission permission) throws Exception;
    //根据前端输入的查询条件进行分页查询
    Page<Permission> findByQueryString(String queryString) throws Exception;
    //根据Id回显权限
    Permission updateById(int id) throws Exception;
    //编辑权限
    void modify(Permission permission) throws Exception;
    //删除前检查该权限是否绑定了角色
    int checkIsWithRole(int id) throws Exception;
    //如果没有绑定,就删除该权限
    void deletePermission(int id) throws Exception;
    //查询所有的权限返回
    List<Permission> findAllPermission();
}
