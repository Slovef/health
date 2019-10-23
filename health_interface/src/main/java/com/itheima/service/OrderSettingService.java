package com.itheima.service;

import com.itheima.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

/**
 * 预约管理
 */
public interface OrderSettingService {

    /**
     * 预约模板上传
     * @param list
     */
    void upload(List<OrderSetting> list) throws Exception;

    /**
     * 查询当月预约列表
     * @param date
     * @return
     */
    List<Map<String, Object>> findAllByDate(String date) throws Exception;

    /**
     * 设置当前日期的预约项
     * @param orderSetting
     * @return
     */
    void editNumberByDate(OrderSetting orderSetting);

    /**
     * 定时清理预约设置
     * @param nowDate
     */
    void cleanOrderSettingOneMonthBefore(String nowDate) throws Exception;
}
