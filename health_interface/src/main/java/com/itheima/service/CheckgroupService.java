package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;

import java.util.List;
import java.util.Map;

/**
 * 检查组管理
 */
public interface CheckgroupService {
    /**
     * 查询所有的检查项返回给新增检查组显示
     * @return
     */
    List<CheckItem> findAllCheckitem() throws Exception;

    /**
     * 添加检查组
     * @param checkGroup
     * @return
     */
    void addCheckGroup(CheckGroup checkGroup,int[] checkitemIds) throws Exception;
    /**
     * 分页查询,填充页面
     * @param queryPageBean
     * @return
     */
    PageResult queryPage(QueryPageBean queryPageBean) throws Exception;

    /**
     * 编辑时回显检查组
     * @param id
     * @return
     */
    Map<String, Object> findById(int id) throws Exception;

    /**
     * 修改检查组合中间表数据
     * @param checkGroup,checkitemIds
     * @return
     */
    void modify(CheckGroup checkGroup, int[] checkitemIds) throws Exception;

    /**
     * 删除检查组合中间表数据
     * @param id
     * @return
     */
    void delete(int id) throws Exception;
}
