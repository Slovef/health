package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckItem;

/**
 * service的接口服务层,用于检查项接口管理
 */
public interface CheckItemService {

    /**
     * 检查项新增
     * @param checkItem 检查项
     */
    void add(CheckItem checkItem) throws Exception;

    /**
     * 检查项分页
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString) throws Exception;

    /**
     * 根据Id显示检查项
     * @param id
     */
    CheckItem updateById(int id) throws Exception;

    /**
     * 编辑检查项
     * @param checkItem
     */
    void modify(CheckItem checkItem) throws Exception;

    /**
     * 删除检查项
     * @param id
     */
    Boolean delete(int id) throws Exception;
}
