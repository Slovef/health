package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constan.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import com.itheima.utils.POIUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 预约管理
 */
@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {

    @Reference
    private OrderSettingService orderSettingService;

    /**
     * 预约上传
     *
     * @param excelFile
     * @return
     */
    @RequestMapping("/upload")
    public Result upload(@RequestParam("excelFile") MultipartFile excelFile) {
        try {
            List<String[]> strings = POIUtils.readExcel(excelFile);
            if (strings != null && strings.size() > 0) {
                List<OrderSetting> list = new ArrayList<OrderSetting>();
                for (String[] string : strings) {
                    Date date = new Date(string[0]);
                    int number = Integer.parseInt(string[1]);
                    OrderSetting orderSetting = new OrderSetting(date, number);
                    list.add(orderSetting);
                }
                System.out.println(list);
                orderSettingService.upload(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return HandleResult.getResult(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
        return HandleResult.getResult(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
    }

    /**
     * 查询当月预约列表
     *
     * @return
     */
    @RequestMapping("/findAllByDate")
    public Result findAllByDate(String date) {
        List<Map<String, Object>> list = null;
        try {
            if (date != null) {
                list = orderSettingService.findAllByDate(date);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return HandleResult.getResult(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
        return HandleResult.getResult(true, MessageConstant.QUERY_ORDER_SUCCESS, list);
    }

    /**
     * 设置当前日期的预约项
     * @param orderDate
     * @param number
     * @return
     */
    @RequestMapping(value = "/editNumberByDate",method = RequestMethod.POST)
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting) {
        try {
            orderSettingService.editNumberByDate(orderSetting);
        } catch (Exception e) {
            e.printStackTrace();
            return HandleResult.getResult(false, MessageConstant.ORDERSETTING_FAIL);
        }
        return HandleResult.getResult(true, MessageConstant.ORDERSETTING_SUCCESS);
    }
}
