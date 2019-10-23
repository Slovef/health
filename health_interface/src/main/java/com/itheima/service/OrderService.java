package com.itheima.service;

import com.itheima.entity.Result;

import java.util.List;
import java.util.Map;

/**
 * 用户微信端预约套餐控制
 */
public interface OrderService {
    /**
     * 用户预约提交
     * @param map
     * @return
     */
    Result submitOder(Map map) throws Exception;

    /**
     * 根据id查询预约Order
     *
     * @param id
     * @return
     */
    Map findById(Integer id) throws Exception;

    /**
     * 各套餐预约占比饼形图
     * @return
     */
    List<Map> getCountOfSetmeal();


}
