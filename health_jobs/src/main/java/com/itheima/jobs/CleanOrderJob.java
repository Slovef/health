package com.itheima.jobs;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.service.OrderSettingService;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CleanOrderJob {

    @Reference
    private OrderSettingService orderSettingService;

    /**
     * 定时清理预约设置
     */

    public void cleanOrderSettingOneMonthBefore() {
        try {
            //获得当天是几月份2019-07
            String nowDate = new SimpleDateFormat("yyyy-MM").format(Calendar.getInstance().getTime());
            orderSettingService.cleanOrderSettingOneMonthBefore(nowDate);
            System.out.println("本月所有预约已清理.........");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
