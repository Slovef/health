package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

/**
 * 持久层,套餐管理
 */
public interface SetmealDao {

    /**
     * 查询所有的检查组返回给新增检查组显示
     * @return
     */
    List<CheckGroup> findAllCheckGroup() throws Exception;

    /**
     * 添加新套餐
     * @param setmeal
     */
    void addSetmeal(Setmeal setmeal) throws Exception;
    //增加新套餐中间表
    void addSetmeal_CheckGroup(Map<String, Integer> map) throws Exception;
    /**
     * 分页查询,填充页面
     * @param queryPageBean
     * @return
     */
    Page<Setmeal> queryPage(String queryPageBean) throws Exception;

    /**
     * 查询所有的套餐项
     * @return
     */
    List<Setmeal> findAllSetmeal() throws Exception;

    /**
     * 点击展示套餐详情
     * @param id
     * @return
     */
    Setmeal findById(int id) throws Exception;
}
