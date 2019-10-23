package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Role;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * 角色管理
 */
public interface RoleService {

    /**
     * 查询所有的菜单和权限返回给新增角色显示
     * @return
     */
    Map<String, List> findAllMenuAndPermission() throws Exception;

    /**
     * 添加角色
     * @param
     * @return
     */
    void addRole(Role role, int[] menuIds, int[] permissionIds) throws Exception;
    /**
     * 分页查询,填充页面
     * @param queryPageBean
     * @return
     */
    PageResult queryPage(QueryPageBean queryPageBean) throws Exception;

    /**
     * 编辑时回显已关联的菜单和权限
     * @param id
     * @return
     */
    Map<String, Object> findById(int id) throws Exception;

    /**
     * 修改角色和中间表数据
     * @param
     * @return
     */
    void modify(Role role, int[] menuIds, int[] permissionIds) throws Exception;

    /**
     * 删除角色和中间表数据
     * @param id
     * @return
     */
    void delete(int id) throws Exception;
}
