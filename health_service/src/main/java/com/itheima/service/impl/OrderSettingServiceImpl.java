package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.OrderSettingDao;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 预约管理
 */

@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;
    /**
     * 预约模板上传
     * @param list
     */
    public void upload(List<OrderSetting> list) throws Exception{
        if (list!=null&&list.size()>0) {
            for (OrderSetting orderSetting : list) {
                doOrderStting(orderSetting);
            }
        }
    }

    //根据OrderSetting判断预约项,有进行修改,无进行新增
    public void doOrderStting(OrderSetting orderSetting) {
        //根据日期判断数据库有无此预约项
        int count = orderSettingDao.checkOrderSetting(orderSetting.getOrderDate());
        //有此预约项,只需要做修改
        if (count > 0) {
            orderSettingDao.updateOrderSetting(orderSetting);
        } else {
            //没有此预约项,需要新增
            orderSettingDao.insertOrderSetting(orderSetting);
        }
    }

    /**
     * 查询当月预约列表
     * @param date
     * @return
     */
    public List<Map<String, Object>> findAllByDate(String date) throws Exception {
        String beginDate = date + "-1";
        String endDate = date + "-31";
        Map selectMap = new HashMap();
        selectMap.put("beginDate", beginDate);
        selectMap.put("endDate", endDate);
        List<OrderSetting> orderLists = orderSettingDao.findAllByDate(selectMap);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (orderLists != null && orderLists.size() > 0) {
            for (OrderSetting orderSetting : orderLists) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("date", orderSetting.getOrderDate().getDate());
                map.put("number", orderSetting.getNumber());
                map.put("reservations", orderSetting.getReservations());
                list.add(map);
            }
        }
        return list;
    }


    /**
     * 设置当前日期的预约项
     * @param orderSetting
     * @return
     */
    public void editNumberByDate(OrderSetting orderSetting) {
        //查询有无当前日期的预约项,有的话修改预约数量,无的话新增该预约项
        doOrderStting(orderSetting);
    }

    /**
     * 定时清理预约设置
     * @param nowDate
     */
    public void cleanOrderSettingOneMonthBefore(String nowDate) throws Exception{
        orderSettingDao.cleanOrderSettingOneMonthBefore(nowDate);
    }

}
