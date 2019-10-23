package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.MenuDao;
import com.itheima.dao.PermissionDao;
import com.itheima.dao.RoleDao;
import com.itheima.dao.UserDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Menu;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

/**
 * 用户管理(登录  权限  等)
 */
@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {


    @Autowired
    private BCryptPasswordEncoder encoder;
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;
    @Autowired
    private MenuDao menuDao;

    /**
     * 通过用户登录的用户名查询用户
     *
     * @param username
     * @return
     */
    public User findByUsername(String username) {
        //根据用户名查询user
        //如果为空返回null
        //如果不为空,根据userId查询Set<Role>
        //遍历roles根据roleId查询Set<Permission>,放到user对象中去
        //把Set<Role>放到user对象中去
        //返回user


        //根据用户名查询user
        User user = userDao.findByUserName(username);
        //如果为空返回null
        if (user == null) {
            return null;
        }
        //如果不为空,根据userId查询Set<Role>
        Set<Role> roles = roleDao.findByUserId(user.getId());
        //遍历roles根据roleId查询Set<Permission>,放到user对象中去
        for (Role role : roles) {
            Set<Permission> permissions = permissionDao.findPermissionByRoleId(role.getId());
            role.setPermissions(permissions);
        }
        //把Set<Role>放到user对象中去
        user.setRoles(roles);
        //返回user
        return user;
    }

    /**
     * 查询所有的角色数据返回给新增用户时显示
     *
     * @return
     */
    public List<Role> findAllRole() throws Exception {
        List<Role> roleList = roleDao.findAllrole();
        return roleList;
    }

    /**
     * 添加用户信息
     *
     * @param
     * @return
     */
    public void addUser(User user, int[] roleIds) throws Exception {
        //查询用户数据库中Id最大值
        Integer id = userDao.getMaxId();
        //+1用于添加新的角色数据
        user.setId(id + 1);
        //用户密码加密
        user.setPassword(encoder.encode(user.getPassword()));
        //新增用户信息
        userDao.addUser(user);
        //新增用户/角色之间的中间表
        Integer userId = user.getId();
        addUserWithRole(roleIds, userId);
    }

    /**
     * 用于新增用户/角色之间的中间表
     *
     * @param roleIds
     * @param userId
     */
    public void addUserWithRole(int[] roleIds, Integer userId) throws Exception {
        if (roleIds != null && roleIds.length > 0) {
            for (int roleId : roleIds) {
                Map map = new HashMap();
                map.put("userId", userId);
                map.put("roleId", roleId);
                userDao.addUser_Role(map);
            }
        }
    }

    /**
     * 分页查询,填充页面
     *
     * @param queryPageBean
     * @return
     */
    public PageResult queryPage(QueryPageBean queryPageBean) throws Exception {

        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        Page<User> pageResult = userDao.queryPage(queryPageBean.getQueryString());
        long total = pageResult.getTotal();
        List<User> result = pageResult.getResult();
        return new PageResult(total, result);
    }

    /**
     * 编辑时回显当前用户信息和已关联的角色信息
     *
     * @param id
     * @return
     */
    public Map<String, Object> findById(int id) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        User user = userDao.findUserById(id);
        List<Integer> roleList = userDao.findRoleIdsById(id);
        map.put("formdata", user);
        map.put("roleIds", roleList);
        return map;
    }

    /**
     * 修改用户和中间表数据
     *
     * @param
     * @return
     */
    public void modify(User user, int[] roleIds) throws Exception {
        Integer userId = user.getId();
        //修改角色数据
        user.setPassword(encoder.encode(user.getPassword()));
        userDao.modifyUser(user);
        //删除关联的中间表
        userDao.deleteUser_Role(userId);
        //新建关联的中间表
        addUserWithRole(roleIds, userId);
    }

    /**
     * 删除用户和中间表数据
     *
     * @param id
     * @return
     */
    public void delete(int id) throws Exception {
        //删除中间表
        userDao.deleteUser_Role(id);
        //删除用户
        userDao.deleteUser(id);
    }

    /**
     * 用户登录后修改密码
     *
     * @param map
     * @param map
     * @return
     * @throws Exception
     */
    public Boolean checkPassword(Map map) throws Exception {
        User user = userDao.checkPass(map);
        if (user != null) {
            userDao.modifyPass(map);
            return true;
        } else {
            return false;
        }
    }
}
