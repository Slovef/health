package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Member;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

/**
 * 套餐管理
 */
public interface SetmealService {
    /**
     * 查询所有的检查组返回给新增检查组显示
     * @return
     */
    List<CheckGroup> findAllCheckGroup() throws Exception;

  /*  *//**
     * 添加套餐
     * @param setmeal,checkgroupIds
     * @return
     */
    void addSetmeal(Setmeal setmeal, int[] checkgroupIds) throws Exception;
    /**
     * 分页查询,填充页面
     * @param queryPageBean
     * @return
     */
    PageResult queryPage(QueryPageBean queryPageBean) throws Exception;
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
