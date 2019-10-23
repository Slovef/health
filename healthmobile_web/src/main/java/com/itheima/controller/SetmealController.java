package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constan.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import com.itheima.utils.HandleResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 套餐管理
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    private SetmealService setmealService;

    /**
     * 查询所有的套餐项
     * @return
     */
    @RequestMapping("/getSetmeal")
    public Result getSetmeal() {
        try {
            List<Setmeal> list = setmealService.findAllSetmeal();
            return HandleResult.getResult(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            return HandleResult.getResult(false, MessageConstant.GET_SETMEAL_LIST_FAIL);
        }
    }

    /**
     * 点击展示套餐详情
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(int id) {
        try {
            Setmeal setmeal = setmealService.findById(id);
            return HandleResult.getResult(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return HandleResult.getResult(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }
}
