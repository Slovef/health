package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constan.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Role;
import com.itheima.service.CheckgroupService;
import com.itheima.service.RoleService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 角色管理
 */
@RestController
@RequestMapping("/role")
public class RoleController {


    @Reference
    private RoleService roleService;

    /**
     * 查询所有的菜单和权限数据返回给新增角色时显示
     * @return Result
     */
    @RequestMapping(value = "/findAllMenuAndPermission",method = RequestMethod.GET)
    public Result findAllCheckitem() {
        try {
            Map<String, List> map = roleService.findAllMenuAndPermission();
            return HandleResult.getResult(true, MessageConstant.GET_MENU_LIST_AND_PERMISSION_LIST_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return HandleResult.getResult(false, MessageConstant.GET_MENU_LIST_AND_PERMISSION_LIST_FAIL);
        }
    }

    /**
     * 新增角色
     * @param
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result add(@RequestBody Role role, int[] menuIds, int[] permissionIds) {
        try {
            roleService.addRole(role,menuIds,permissionIds);
            return HandleResult.getResult(true, MessageConstant.ADD_ROLE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return HandleResult.getResult(false, MessageConstant.ADD_ROLEU_FAIL);
        }
    }

    /**
     * 分页查询,填充页面
     * @param queryPageBean
     * @return
     */
    @RequestMapping(value = "/queryPage", method = RequestMethod.POST)
    public Result queryPage(@RequestBody QueryPageBean queryPageBean) {
        try {
            PageResult pageResult = roleService.queryPage(queryPageBean);
            return HandleResult.getResult(true, MessageConstant.QUERY_ROLE_SUCCESS,pageResult);
        } catch (Exception e) {
            e.printStackTrace();
            return HandleResult.getResult(false, MessageConstant.QUERY_ROLE_FAIL);
        }
    }

    /**
     * 编辑时回显已关联的菜单和权限
     * @param roleId
     * @return
     */
    @RequestMapping(value = "/findSelectedMenuAndPermById",method = RequestMethod.GET)
    public Result findById(int roleId) {
        try {
            Map<String, Object> map = roleService.findById(roleId);
            return HandleResult.getResult(true,MessageConstant.QUERY_ROLE_SUCCESS,map );
        } catch (Exception e) {
            e.printStackTrace();
            return HandleResult.getResult(false,MessageConstant.QUERY_ROLE_FAIL );
        }
    }

    /**
     * 修改角色和中间表数据
     * @param
     * @return
     */
    @RequestMapping(value = "/modify",method = RequestMethod.POST)
    public Result modify(@RequestBody Role role, int[] menuIds, int[] permissionIds) {
        try {
            roleService.modify(role, menuIds,permissionIds);
            return HandleResult.getResult(true,MessageConstant.EDIT_ROLE_SUCCESS );
        } catch (Exception e) {
            e.printStackTrace();
            return HandleResult.getResult(false,MessageConstant.EDIT_ROLE_FAIL );
        }
    }

    /**
     * 删除角色和中间表数据
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    public Result delete(int id) {
        try {
            roleService.delete(id);
            return HandleResult.getResult(true,MessageConstant.DELETE_ROLE_SUCCESS );
        } catch (Exception e) {
            e.printStackTrace();
            return HandleResult.getResult(false,MessageConstant.DELETE_ROLE_FAIL );
        }
    }


}
