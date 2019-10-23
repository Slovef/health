package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constan.MessageConstant;
import com.itheima.constan.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Order;
import com.itheima.pojo.Setmeal;
import com.itheima.service.OrderService;
import com.itheima.service.SetmealService;
import com.itheima.utils.HandleResult;
import com.itheima.utils.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private OrderService orderService;

    @Reference
    private SetmealService setmealService;

    /**
     * 用户预约提交
     *
     * @param map
     * @return
     */
    @RequestMapping("/submit")
    public Result submit(@RequestBody Map map) throws Exception {
        Result result = null;
        //获得用户名
        String name = (String) map.get("name");
        //获取手机号码
        String telephone = (String) map.get("telephone");
        //获得用户输入的日期
        String orderDate = (String) map.get("orderDate");
        //获取redis中的验证码
//        String redisValidateCode = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        //获取用户输入验证码
        String validateCode = (String) map.get("validateCode");

        //判断用户名是否为空
        if (name == null || name.equals("")) {
            return HandleResult.getResult(false, MessageConstant.NAME_IS_EMPTY);
        }

        //判断预约日期是否为空
        if (orderDate == null || orderDate.equals("")) {
            return HandleResult.getResult(false, MessageConstant.SELECTED_DATE_CANNOT_EMPTY);
        }

        //判断用户输入的验证码和手机号是否为空
        if (validateCode == null || validateCode.equals("") || telephone == null || telephone.equals("")) {
            return HandleResult.getResult(false, MessageConstant.TELEPHONE_VALIDATECODE_NOTNULL);
        }

        /*//判断redis中的验证码是否已销毁清空
        if ((validateCode != null && !validateCode.equals("")) && (redisValidateCode == null || redisValidateCode == "")) {
            return HandleResult.getResult(false, MessageConstant.VALIDATECODE_OUTDATE);
        }
        //判断用户输入的验证码和redis中的验证码是否一致
        if ((validateCode != null && !validateCode.equals("")) && (!validateCode.equals(redisValidateCode))) {
            return HandleResult.getResult(false, MessageConstant.VALIDATECODE_ERROR);
        } else {*/
        //用户提交预约,调用service进行处理
        try {
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
            result = orderService.submitOder(map);
        } catch (Exception e) {
            e.printStackTrace();
            return HandleResult.getResult(false, MessageConstant.ORDER_FAIL);
        }
        //用户如果预约成功,发送通知短信给用户
      /*  if (result.isFlag()) {
//            Setmeal setmeal = setmealService.findById(Integer.parseInt(map.get("setmealId").toString()));
            SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE, telephone,orderDate);
        }*/

//        }
        return result;
    }

    /**
     * 根据id查询预约Order
     *
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        Map map = null;
        try {
            map = orderService.findById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return HandleResult.getResult(false, MessageConstant.QUERY_ORDER_FAIL);
        }
        return HandleResult.getResult(true, MessageConstant.QUERY_ORDER_SUCCESS, map);
    }
}
