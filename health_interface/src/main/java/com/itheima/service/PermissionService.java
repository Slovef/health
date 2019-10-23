package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Permission;

import java.util.List;

/**
 * permission的接口服务层,用于权限管理
 */
public interface PermissionService {

    /**
     * 检查项新增
     * @param permission 检查项
     */
    void add(Permission permission) throws Exception;

    /**
     * 检查项分页
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString) throws Exception;

    /**
     * 根据Id显示权限
     * @param id
     */
    Permission updateById(int id) throws Exception;

    /**
     * 编辑权限
     * @param permission
     */
    void modify(Permission permission) throws Exception;

    /**
     * 删除权限
     * @param id
     */
    Boolean delete(int id) throws Exception;

}
