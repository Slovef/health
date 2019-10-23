package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constan.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * 检查项管理
 */
@RestController
@RequestMapping("/checkitem")
public class CheckItenController {

    @Reference
    private CheckItemService checkItemService;

    /**
     * 新增检查项
     * @param checkItem
     * @return
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public Result add(@RequestBody CheckItem checkItem ) {
        try {
            checkItemService.add(checkItem);
            return  HandleResult.getResult(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return  HandleResult.getResult(false, MessageConstant.ADD_CHECKGROUP_FAIL);

        }
    }

    /**
     * 检查项分页
     * @param queryPageBean
     * @return
     */
    @PreAuthorize("hasAnyAuthority('CHECKITEM_QUERY')")
    @RequestMapping(value = "/findPage",method = RequestMethod.POST)
    public Result findPage(@RequestBody QueryPageBean queryPageBean) {
        try {
            PageResult pageResult = checkItemService.findPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize(),queryPageBean.getQueryString());
            return HandleResult.getResult(true, "分页查询成功!", pageResult);
        } catch (Exception e) {
            e.printStackTrace();
            return HandleResult.getResult(false, "分页查询失败!");
        }
    }

    /**
     * 根据Id显示检查项
     * @param id
     * @return
     */
    @RequestMapping(value = "/updateById",method = RequestMethod.GET)
    public Result updateById(int id) {
        try {
            CheckItem checkItem = checkItemService.updateById(id);
            return  HandleResult.getResult(true,MessageConstant.QUERY_CHECKITEM_SUCCESS ,checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            return  HandleResult.getResult(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }

    /**
     * 修改检查项
     * @param checkItem
     * @return
     */
    @RequestMapping(value = "/modify",method = RequestMethod.POST)
    public Result modify(@RequestBody CheckItem checkItem) {
        try {
            checkItemService.modify(checkItem);
            return  HandleResult.getResult(true,MessageConstant.EDIT_CHECKITEM_SUCCESS );
        } catch (Exception e) {
            e.printStackTrace();
            return  HandleResult.getResult(false, MessageConstant.EDIT_CHECKITEM_FAIL);
        }
    }

    /**
     * 删除检查项
     * @param id
     */
    @PreAuthorize("hasAnyAuthority('CHECKITEM_DELETE')")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public Result delete(int id) {
        try {
            Boolean isCheckGroup = checkItemService.delete(id);
            if (isCheckGroup) {
                return  HandleResult.getResult(true,MessageConstant.DELETE_CHECKITEM_SUCCESS );
            } else {
                return HandleResult.getResult(true, MessageConstant.DELETE_CHECKITEM_FAIL_HAVE_CHECKGROUP);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return  HandleResult.getResult(false, MessageConstant.DELETE_CHECKITEM_FAIL);
        }
    }



}
