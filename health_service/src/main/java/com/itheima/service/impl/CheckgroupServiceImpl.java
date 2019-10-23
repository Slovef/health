package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckgroupDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckgroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 业务层,检查组管理
 * interfaceClass:使用的Cglib产生的代理对象的类型是HelloService类型
 */
@Service(interfaceClass = CheckgroupService.class)
@Transactional
public class CheckgroupServiceImpl implements CheckgroupService {

    @Autowired
    private CheckgroupDao checkgroupDao;

    /**
     * 查询所有的检查项返回给新增检查组显示
     *
     * @return
     */
    public List<CheckItem> findAllCheckitem() throws Exception {
        List<CheckItem> checkItem = checkgroupDao.findAllCheckitem();
        return checkItem;
    }

    /**
     * 添加检查组
     *
     * @param checkGroup
     * @return
     */
    public void addCheckGroup(CheckGroup checkGroup, int[] checkitemIds) throws Exception {
        //新增检查组信息
        checkgroupDao.addCheckGroup(checkGroup);
//        System.out.println(checkGroup.getId());
        Integer checkGroupId = checkGroup.getId();
        //新增中间表
        if (checkitemIds != null && checkitemIds.length > 0) {
            for (int checkitemId : checkitemIds) {
                Map<String, Integer> map = new HashMap<String, Integer>();
                map.put("checkgroup_id", checkGroupId);
                map.put("checkitem_id", checkitemId);
                checkgroupDao.addCheckitemIds(map);
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
        Page<CheckGroup> pageResult = checkgroupDao.queryPage(queryPageBean.getQueryString());
        long total = pageResult.getTotal();
        List<CheckGroup> result = pageResult.getResult();
        return new PageResult(total,result);
    }

    /**
     * 编辑时回显检查组
     * @param id
     * @return
     */
    public Map<String, Object> findById(int id) throws Exception{
        Map<String, Object> map = new HashMap<String, Object>();
        CheckGroup checkGroup = checkgroupDao.findCheckgroupById(id);
        Integer[] arr = checkgroupDao.findCheckitemIdsById(id);
        map.put("formdata", checkGroup);
        map.put("checkitemids", arr);
        return map;
    }

    /**
     * 修改检查组合中间表数据
     * @param checkGroup,checkitemIds
     * @return
     */
    public void modify(CheckGroup checkGroup, int[] checkitemIds) throws Exception {
        Integer id = checkGroup.getId();
        //修改检查组数据
        checkgroupDao.modifyCheckGroup(checkGroup);
        //删除关联的中间表
        checkgroupDao.deleteCheckGroup_CheckItem(id);
        //新建关联的中间表
        for (int checkitemId : checkitemIds) {
            Map<String, Integer> map = new HashMap<String, Integer>();
            map.put("checkgroup_id", id);
            map.put("checkitem_id", checkitemId);
            checkgroupDao.addCheckGroup_CheckItem(map);
        }
    }

    /**
     * 删除检查组合中间表数据
     * @param id
     * @return
     */
    public void delete(int id) throws Exception {
        //删除中间表
        checkgroupDao.deleteCheckGroup_CheckItem(id);
        //删除检查组
        checkgroupDao.deleteCheckGroup(id);
    }
}
