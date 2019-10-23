package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constan.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Menu;
import com.itheima.pojo.Permission;
import com.itheima.service.MenuService;
import com.itheima.service.PermissionService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 菜单管理
 */
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Reference
    private PermissionService permissionService;
    @Reference
    private MenuService menuService;

    /**
     * 根据用户登录的用户名查询该用户下的所有菜单项
     * @param
     * @return
     */
    @RequestMapping(value = "/getUserMenu")
    public Result getUserMenu() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = user.getUsername();
        try {
            List<Map> list = menuService.getUserMenu(username);
            return HandleResult.getResult(true, MessageConstant.GET_MENU_SUCCESS, list);
        } catch (Exception e) {
            e.printStackTrace();
            return HandleResult.getResult(false, MessageConstant.GET_MENU_FAIL);
        }
    }

    /**
     * 新增菜单
     * @param menu
     * @return
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public Result add(@RequestBody Menu menu ) {
        try {
            menuService.add(menu);
            return  HandleResult.getResult(true, MessageConstant.ADD_MENU_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return  HandleResult.getResult(false, MessageConstant.ADD_MENU_FAIL);

        }
    }

    /**
     * 所有菜单项分页查询
     * @param queryPageBean
     * @return
     */
    @RequestMapping(value = "/findPage",method = RequestMethod.POST)
    public Result findPage(@RequestBody QueryPageBean queryPageBean) {
        try {
            PageResult pageResult = menuService.findPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize(),queryPageBean.getQueryString());
            return HandleResult.getResult(true, MessageConstant.GET_MENUL_IST_SUCCESS, pageResult);
        } catch (Exception e) {
            e.printStackTrace();
            return HandleResult.getResult(false, MessageConstant.GET_MENU_LIST_FAIL);
        }
    }

    /**
     * 根据Id回显菜单
     * @param id
     * @return
     */
    @RequestMapping(value = "/updateById",method = RequestMethod.GET)
    public Result updateById(int id) {
        try {
            Menu menu = menuService.updateById(id);
            return  HandleResult.getResult(true,MessageConstant.QUERY_MENU_SUCCESS ,menu);
        } catch (Exception e) {
            e.printStackTrace();
            return  HandleResult.getResult(false, MessageConstant.QUERY_MENU_FAIL);
        }
    }

    /**
     * 修改菜单
     * @param menu
     * @return
     */
    @RequestMapping(value = "/modify",method = RequestMethod.POST)
    public Result modify(@RequestBody Menu menu) {
        try {
            menuService.modify(menu);
            return  HandleResult.getResult(true,MessageConstant.EDIT_MENU_SUCCESS );
        } catch (Exception e) {
            e.printStackTrace();
            return  HandleResult.getResult(false, MessageConstant.EDIT_MENU_FAIL);
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
            Boolean isWithRole = menuService.delete(id);
            if (isWithRole) {
                return  HandleResult.getResult(true,MessageConstant.DELETE_MENU_SUCCESS );
            } else {
                return HandleResult.getResult(true, MessageConstant.DELETE_MENU_FAIL_WITH_ROLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return  HandleResult.getResult(false, MessageConstant.DELETE_MENU_FAIL);
        }
    }


}
