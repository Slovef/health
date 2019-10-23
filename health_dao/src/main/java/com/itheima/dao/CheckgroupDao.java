package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;

import java.util.List;
import java.util.Map;

/**
 * 持久层,检查组管理
 */
public interface CheckgroupDao {

    /**
     * 查询所有的检查项返回给新增检查组显示
     *
     * @return
     */
    List<CheckItem> findAllCheckitem() throws Exception;

    /**
     * 添加检查组
     *
     * @param checkGroup
     * @return
     */
    void addCheckGroup(CheckGroup checkGroup) throws Exception;

    //新增中间表
    void addCheckitemIds(Map<String, Integer> map) throws Exception;

    /**
     * 分页查询,填充页面
     *
     * @param queryPageBean
     * @return
     */
    Page<CheckGroup> queryPage(String queryPageBean) throws Exception;

    /**
     * 编辑时回显检查组
     *
     * @param id
     * @return
     */
    CheckGroup findCheckgroupById(int id) throws Exception;

    Integer[] findCheckitemIdsById(int id) throws Exception;

    /**
     * 修改检查组合中间表数据
     *
     * @param checkGroup,checkitemIds
     * @return
     */
    //修改检查组数据
    void modifyCheckGroup(CheckGroup checkGroup) throws Exception;

    //删除关联的中间表
    void deleteCheckGroup_CheckItem(Integer id) throws Exception;

    //新建关联的中间表
    void addCheckGroup_CheckItem(Map<String, Integer> map) throws Exception;

    /**
     * 删除检查组合中间表数据
     *
     * @param id
     * @return
     */
    void deleteCheckGroup(int id) throws Exception;

}
