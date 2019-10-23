package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckgroupDao;
import com.itheima.dao.MenuDao;
import com.itheima.dao.PermissionDao;
import com.itheima.dao.RoleDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.*;
import com.itheima.service.CheckgroupService;
import com.itheima.service.MenuService;
import com.itheima.service.PermissionService;
import com.itheima.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 业务层,角色管理
 * interfaceClass:使用的Cglib产生的代理对象的类型是HelloService类型
 */
@Service(interfaceClass = RoleService.class)
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    private MenuDao menuDao;
    @Autowired
    private PermissionDao permissionDao;
    @Autowired
    private RoleDao roleDao;

    /**
     * 查询所有的检查项返回给新增检查组显示
     *
     * @return
     */
    public Map<String, List> findAllMenuAndPermission() throws Exception {
        Map<String, List> map = new HashMap<String, List>();
        //查询所有的菜单返回
        List<Menu> menus = menuDao.findAllMenu();
        //查询所有的权限返回
        List<Permission> permissions = permissionDao.findAllPermission();
        //添加到map中
        map.put("menuData", menus);
        map.put("permissionData", permissions);
        return map;
    }

    /**
     * 添加角色
     *
     * @param
     * @return
     */
    public void addRole(Role role, int[] menuIds, int[] permissionIds) throws Exception {
        //查询数据库中Id最大值
        Integer id = roleDao.getMaxId(role);
        //+1用于添加新的角色数据
        role.setId(id+1);
        //新增角色信息
        roleDao.addRole(role);
        //获得新增角色的Id
        Integer roleId = role.getId();
        //新增菜单/权限和角色之间的中间表
        addMenuAndPermWithRole(menuIds,permissionIds,roleId);

    }

    /**
     * 用于新增菜单/权限和角色之间的中间表
     * @param menuIds
     * @param permissionIds
     */
    public void addMenuAndPermWithRole(int[] menuIds, int[] permissionIds,Integer roleId) throws Exception {
        if (menuIds != null && menuIds.length > 0) {
            for (int menuId : menuIds) {
                Map map = new HashMap();
                map.put("menuId", menuId);
                map.put("roleId", roleId);
                roleDao.addMenu_Role(map);
            }
        }
        if (permissionIds != null && permissionIds.length > 0) {
            for (int permissionId : permissionIds) {
                Map map = new HashMap();
                map.put("permissionId", permissionId);
                map.put("roleId", roleId);
                roleDao.addPermission_Role(map);
            }
        }
    }

    /**
     * 分页查询,填充页面
     * @param queryPageBean
     * @return
     */
    public PageResult queryPage(QueryPageBean queryPageBean) throws Exception {

        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        Page<Role> pageResult = roleDao.queryPage(queryPageBean.getQueryString());
        long total = pageResult.getTotal();
        List<Role> result = pageResult.getResult();
        return new PageResult(total,result);
    }

    /**
     * 编辑时回显已关联的菜单和权限
     * @param id
     * @return
     */
    public Map<String, Object> findById(int id) throws Exception{
        Map<String, Object> map = new HashMap<String, Object>();
        Role role = roleDao.findRoleById(id);
        Integer[] menuArr = roleDao.findMenuIdsById(id);
        Integer[] permissionArr = roleDao.findPermissionIdsById(id);
        map.put("formdata", role);
        map.put("menuIds", menuArr);
        map.put("permissionIds", permissionArr);
        return map;

    }

    /**
     * 修改角色和中间表数据
     * @param
     * @return
     */
    public void modify(Role role, int[] menuIds, int[] permissionIds) throws Exception {
        Integer roleId = role.getId();
        //修改角色数据
        roleDao.modifyRole(role);
        //删除关联的中间表
        roleDao.deleteRole_Menu(roleId);
        roleDao.deleteRole_Permission(roleId);
        //新建关联的中间表
       addMenuAndPermWithRole(menuIds,permissionIds,roleId);
    }

    /**
     * 删除角色和中间表数据
     * @param id
     * @return
     */
    public void delete(int id) throws Exception {
        //删除中间表
        roleDao.deleteRole_Menu(id);
        roleDao.deleteRole_Permission(id);
        //删除角色
        roleDao.deleteRole(id);
    }
}
