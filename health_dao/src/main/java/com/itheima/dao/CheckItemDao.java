package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckItem;

/**
 * 持久层,检查项管理接口
 */
public interface CheckItemDao {

    //新增检查项
    void addCheckItem(CheckItem checkItem) throws Exception;
    //根据前端输入的查询条件进行分页查询
    Page<CheckItem> findByQueryString(String queryString) throws Exception;
    //根据Id显示检查项
    CheckItem updateById(int id) throws Exception;
    //编辑检查项
    void modify(CheckItem checkItem) throws Exception;
    //删除前检查该检查项是否绑定了检查组
    int checkIsGroup(int id) throws Exception;
    //如果没有绑定,就删除该检查项
    void deleteCheckItem(int id) throws Exception;

}
