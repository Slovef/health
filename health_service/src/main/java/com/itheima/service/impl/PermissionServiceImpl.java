package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckItemDao;
import com.itheima.dao.PermissionDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Permission;
import com.itheima.service.CheckItemService;
import com.itheima.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 业务层,权限管理
 * interfaceClass:使用的Cglib产生的代理对象的类型是CheckItemService类型
 */
@Service(interfaceClass = PermissionService.class)
@Transactional
public class PermissionServiceImpl implements PermissionService {


    @Autowired
    private PermissionDao permissionDao;

    /**
     * 新增检查项
     * @param permission 检查项
     */
    public void add(Permission permission) throws Exception{
//        System.out.println(1/0);
        permissionDao.addPermission(permission);
    }

    /**
     * 检查项分页
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     * @throws Exception
     */
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) throws Exception {
        //Mybatis的分页查询助手,PageHelper设置分页参数 紧跟着的第二行代码一定要进行分页的语句代码
        PageHelper.startPage(currentPage,pageSize);
        Page<Permission> permissionPage = permissionDao.findByQueryString(queryString);
        long total = permissionPage.getTotal();
        List<Permission> result = permissionPage.getResult();
        return new PageResult(total, result);
    }

    /**
     * 编辑权限
     * @param id
     * @throws Exception
     */
    public Permission updateById(int id) throws Exception {
        Permission permission = permissionDao.updateById(id);
        return permission;
    }

    /**
     * 编辑权限
     * @param permission
     * @throws Exception
     */
    public void modify(Permission permission) throws Exception {
        permissionDao.modify(permission);
    }

    /**
     * 删除权限
     * @param id
     */
    public Boolean delete(int id) throws Exception {
        //查询是否是否绑定角色
        int count = permissionDao.checkIsWithRole(id);
        if (count > 0) {
            return false;
        } else {
            permissionDao.deletePermission(id);
            return true;
        }
    }

}
