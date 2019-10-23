package com.itheima.dao;

import com.itheima.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 预约管理
 */
public interface OrderSettingDao {
    /**
     * 预约模板上传
     * @param orderDate
     * @return
     */
    //检查有无此预约项
    int checkOrderSetting(Date orderDate);
    //有,进行修改
    void updateOrderSetting(OrderSetting orderSetting);
    //无进行新增
    void insertOrderSetting(OrderSetting orderSetting);
    /**
     * 查询当月预约列表
     * @param map
     * @return
     */
    List<OrderSetting> findAllByDate(Map map);



    //根据用户手机端预约输入的日期查询有无此预约项
    OrderSetting findByDate(Date date);
    //已预约人数+1
    void addOneInReservations(OrderSetting orderSetting);

    //定时清理预约设置
    void cleanOrderSettingOneMonthBefore(String nowDate) throws Exception;
}
