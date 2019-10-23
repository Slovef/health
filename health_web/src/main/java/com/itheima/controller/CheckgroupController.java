package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constan.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckgroupService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 检查组管理
 */
@RestController
@RequestMapping("/checkgroup")
public class CheckgroupController {

    @Reference
    private CheckgroupService checkgroupService;

    /**
     * 查询所有的检查项返回给新增检查组显示
     * @return Result
     */
    @RequestMapping(value = "/findAllCheckitem",method = RequestMethod.GET)
    public Result findAllCheckitem() {
        try {
            List<CheckItem> checkItem = checkgroupService.findAllCheckitem();
            return HandleResult.getResult(true, MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            return HandleResult.getResult(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }

    /**
     * 添加检查组
     * @param checkGroup
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result add(@RequestBody CheckGroup checkGroup,int[] checkitemIds) {
        try {
            checkgroupService.addCheckGroup(checkGroup,checkitemIds);
            return HandleResult.getResult(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return HandleResult.getResult(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
    }

    /**
     * 分页查询,填充页面
     * @param queryPageBean
     * @return
     */
    @RequestMapping(value = "/queryPage", method = RequestMethod.POST)
    public Result queryPage(@RequestBody QueryPageBean queryPageBean) {
        try {
            PageResult pageResult = checkgroupService.queryPage(queryPageBean);
            return HandleResult.getResult(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,pageResult);
        } catch (Exception e) {
            e.printStackTrace();
            return HandleResult.getResult(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

    /**
     * 编辑时回显检查组
     * @param id
     * @return
     */
    @RequestMapping(value = "/findById",method = RequestMethod.GET)
    public Result findById(int id) {
        try {
            Map<String, Object> map = checkgroupService.findById(id);
            return HandleResult.getResult(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,map );
        } catch (Exception e) {
            e.printStackTrace();
            return HandleResult.getResult(false,MessageConstant.QUERY_CHECKGROUP_FAIL );
        }
    }

    /**
     * 修改检查组合中间表数据
     * @param checkGroup,checkitemIds
     * @return
     */
    @RequestMapping(value = "/modify",method = RequestMethod.POST)
    public Result modify(@RequestBody CheckGroup checkGroup,int[] checkitemIds) {
        try {
            checkgroupService.modify(checkGroup, checkitemIds);
            return HandleResult.getResult(true,MessageConstant.EDIT_CHECKGROUP_SUCCESS );
        } catch (Exception e) {
            e.printStackTrace();
            return HandleResult.getResult(false,MessageConstant.EDIT_CHECKGROUP_FAIL );
        }
    }

    /**
     * 删除检查组合中间表数据
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    public Result delete(int id) {
        try {
            checkgroupService.delete(id);
            return HandleResult.getResult(true,MessageConstant.DELETE_CHECKGROUP_SUCCESS );
        } catch (Exception e) {
            e.printStackTrace();
            return HandleResult.getResult(false,MessageConstant.DELETE_CHECKGROUP_FAIL );
        }
    }


}
