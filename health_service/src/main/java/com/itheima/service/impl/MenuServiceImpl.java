package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.MenuDao;
import com.itheima.dao.PermissionDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.Menu;
import com.itheima.pojo.Permission;
import com.itheima.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 菜单管理
 */
@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuDao menuDao;


    /**
     * 根据用户登录的用户名查询该用户下的所有菜单项
     *
     * @param username
     * @return
     */
    public List<Map> getUserMenu(String username) {

        //根据用户名查角色,根据角色查菜单,得到当前用户的所有菜单项存到list<Menu>中
        List<Menu> allMenuList = menuDao.getAllMenuByUsername(username);
        //创建一/二/三级菜单集合
        List<Menu> level1 = new ArrayList<Menu>();
        List<Menu> level2 = new ArrayList<Menu>();
        List<Menu> level3 = new ArrayList<Menu>();
        //然后遍历所有菜单,用父菜单的id=parentMenuld去查询当前父菜单下的所有子菜单,然后添加到集合中去
        if (allMenuList != null && allMenuList.size() > 0) {
            for (Menu menu : allMenuList) {
                //一般差不多也就三级菜单
                switch (menu.getLevel()) {
                    case 1:
                        level1.add(menu);
                        break;
                    case 2:
                        level2.add(menu);
                        break;
                    case 3:
                        level3.add(menu);
                        break;
                }
            }
        }


        //一级菜单的集合
        List<Map> list1 = new ArrayList<Map>();
        if (level1 != null && level1.size() > 0) {
            for (Menu menu1 : level1) {
                //二级菜单的集合(每遍历一次一级菜单就需要new 一个新的list2对象出来)
                List<Map> list2 = new ArrayList<Map>();
                //判断有没有二级菜单,有的话遍历
                if (level2 != null && level2.size() > 0) {
                    Integer id1 = menu1.getId();
                    for (Menu menu2 : level2) {
                        //三级菜单的集合(每遍历一次二级菜单就需要new 一个新的list3对象出来)
                        List<Map> list3 = new ArrayList<Map>();
                        Integer parentMenuId2 = menu2.getParentMenuId();
                        //判断有没有三级菜单,有的话遍历
                        if (level3 != null && level3.size() > 0) {
                            Integer id2 = menu2.getId();
                            for (Menu menu3 : level3) {
                                Integer parentMenuId3 = menu3.getParentMenuId();
                                //三级菜单的添加
                                if (id1 == parentMenuId2 && id2 == parentMenuId3) {
                                    Map map3 = getChildrenMenuMap(menu3, null);
                                    list3.add(map3);
                                }
                            }
                        }
                        //二级菜单的添加,该菜单的添加是在三级菜单添加之后添加的
                        if (parentMenuId2 == id1) {
                            Map map2 = getChildrenMenuMap(menu2, list3);
                            list2.add(map2);
                        }

                    }
                }
                //一级菜单的添加,该菜单的添加是在二级菜单和三级菜单添加之后添加的
                Map map1 = getParentMenuMap(menu1, list2);
                list1.add(map1);
            }
        }
        //返回一级菜单(包含二级菜单和三级菜单)
        return list1;
    }

    /**
     * 添加子菜单List<map>集合的方法
     * @param menu
     * @param children
     * @return
     */
    public Map getChildrenMenuMap(Menu menu, List<Map> children) {
        Map map = new HashMap();
        map.put("path", menu.getPath());
        map.put("title", menu.getName());
        map.put("linkUrl", menu.getLinkUrl());
        map.put("children", children);
        return map;
    }

    /**
     * 添加父菜单List<map>集合的方法
     * @param menu
     * @param children
     * @return
     */
    public Map getParentMenuMap(Menu menu, List<Map> children) {
        Map map = new HashMap();
        map.put("path", menu.getPath());
        map.put("title", menu.getName());
        map.put("icon", menu.getIcon());
        map.put("children", children);
        return map;
    }



    @Autowired
    private PermissionDao permissionDao;

    /**
     * 新增菜单
     * @param menu
     */
    public void add(Menu menu) throws Exception{
        menuDao.addMenu(menu);
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
        Page<Menu> permissionPage = menuDao.findByQueryString(queryString);
        long total = permissionPage.getTotal();
        List<Menu> result = permissionPage.getResult();
        return new PageResult(total, result);
    }

    /**
     * 编辑菜单
     * @param id
     * @throws Exception
     */
    public Menu updateById(int id) throws Exception {
        Menu menu = menuDao.updateById(id);
        return menu;
    }

    /**
     * 修改菜单
     * @param menu
     * @throws Exception
     */
    public void modify(Menu menu) throws Exception {
        menuDao.modify(menu);
    }

    /**
     * 删除菜单
     * @param id
     */
    public Boolean delete(int id) throws Exception {
        //查询是否是否绑定角色
        int count = menuDao.checkIsWithRole(id);
        if (count > 0) {
            return false;
        } else {
            menuDao.deleteMenu(id);
            return true;
        }
    }



}
