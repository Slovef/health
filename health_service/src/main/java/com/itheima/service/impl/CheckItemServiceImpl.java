package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckItemDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 业务层,检查项管理
 * interfaceClass:使用的Cglib产生的代理对象的类型是CheckItemService类型
 */
@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    private CheckItemDao checkItemDao;

    /**
     * 新增检查项
     * @param checkItem 检查项
     */
    public void add(CheckItem checkItem) throws Exception{
//        System.out.println(1/0);
        checkItemDao.addCheckItem(checkItem);
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
        Page<CheckItem> checkItemPage = checkItemDao.findByQueryString(queryString);
        long total = checkItemPage.getTotal();
        List<CheckItem> result = checkItemPage.getResult();
        return new PageResult(total, result);
    }

    /**
     * 编辑检查项
     * @param id
     * @throws Exception
     */
    public CheckItem updateById(int id) throws Exception {
        CheckItem checkItem = checkItemDao.updateById(id);
        return checkItem;
    }

    /**
     * 编辑检查项
     * @param checkItem
     * @throws Exception
     */
    public void modify(CheckItem checkItem) throws Exception {
        checkItemDao.modify(checkItem);
    }

    /**
     * 删除检查项
     * @param id
     */
    public Boolean delete(int id) throws Exception {
        //查询是否是否绑定检查组
        int count = checkItemDao.checkIsGroup(id);
        if (count > 0) {
            return false;
        } else {
            checkItemDao.deleteCheckItem(id);
            return true;
        }
    }


}
