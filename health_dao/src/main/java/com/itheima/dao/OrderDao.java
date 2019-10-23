package com.itheima.dao;

import com.itheima.pojo.Order;

import java.util.List;
import java.util.Map;

/**
 * 用户微信端预约套餐控制
 */
public interface OrderDao {

    //判断用户是否已经预约过该套餐
    List<Order> isOrdered(Order order) throws Exception;
    //没有预约过,就添加新的预约
    void addOrderByOrder(Order order) throws Exception;
    //根据id查询预约Order
    Map findById(Integer id) throws Exception;
    //各套餐预约占比饼形图
    List<Map> getCountOfSetmeal();
    //热门套餐（取前4）
    List<Map> getHotSetmealList();
    //今日预约数
    Integer findOrderCountByDate(String today);
    //本周预约数
    Integer findOrderCountAfterDate(String thisWeekMonday);
    //今日到诊数
    Integer findVisitsCountByDate(String today);
    //本周到诊数
    Integer findVisitsCountAfterDate(String thisWeekMonday);


}
