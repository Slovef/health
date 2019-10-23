package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constan.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Permission;
import com.itheima.service.CheckItemService;
import com.itheima.service.PermissionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * 权限管理
 */
@RestController
@RequestMapping("/permission")
public class PermissionController {


    @Reference
    private PermissionService permissionService;

    /**
     * 新增权限
     * @param permission
     * @return
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public Result add(@RequestBody Permission permission ) {
        try {
            permissionService.add(permission);
            return  HandleResult.getResult(true, MessageConstant.ADD_PERMISSION_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return  HandleResult.getResult(false, MessageConstant.ADD_PERMISSION_FAIL);

        }
    }

    /**
     * 权限分页
     * @param queryPageBean
     * @return
     */
    @RequestMapping(value = "/findPage",method = RequestMethod.POST)
    public Result findPage(@RequestBody QueryPageBean queryPageBean) {
        try {
            PageResult pageResult = permissionService.findPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize(),queryPageBean.getQueryString());
            return HandleResult.getResult(true, MessageConstant.GET_PERMISSION_LIST_SUCCESS, pageResult);
        } catch (Exception e) {
            e.printStackTrace();
            return HandleResult.getResult(false, MessageConstant.GET_PERMISSION_LIST_FAIL);
        }
    }

    /**
     * 根据Id显示权限
     * @param id
     * @return
     */
    @RequestMapping(value = "/updateById",method = RequestMethod.GET)
    public Result updateById(int id) {
        try {
            Permission permission = permissionService.updateById(id);
            return  HandleResult.getResult(true,MessageConstant.QUERY_PERMISSION_SUCCESS ,permission);
        } catch (Exception e) {
            e.printStackTrace();
            return  HandleResult.getResult(false, MessageConstant.QUERY_PERMISSION_FAIL);
        }
    }

    /**
     * 修改权限
     * @param permission
     * @return
     */
    @RequestMapping(value = "/modify",method = RequestMethod.POST)
    public Result modify(@RequestBody Permission permission) {
        try {
            permissionService.modify(permission);
            return  HandleResult.getResult(true,MessageConstant.EDIT_PERMISSION_SUCCESS );
        } catch (Exception e) {
            e.printStackTrace();
            return  HandleResult.getResult(false, MessageConstant.EDIT_PERMISSION_FAIL);
        }
    }

    /**
     * 删除权限
     * @param id
     */
//    @PreAuthorize("hasAnyAuthority('CHECKITEM_DELETE')")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public Result delete(int id) {
        try {
            Boolean isWithRole = permissionService.delete(id);
            if (isWithRole) {
                return  HandleResult.getResult(true,MessageConstant.DELETE_PERMISSION_SUCCESS );
            } else {
                return HandleResult.getResult(true, MessageConstant.DELETE_PERMISSION_FAIL_WITH_ROLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return  HandleResult.getResult(false, MessageConstant.DELETE_PERMISSION_FAIL);
        }
    }



}
