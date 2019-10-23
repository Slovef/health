package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constan.MessageConstant;
import com.itheima.constan.RedisConstant;
import com.itheima.constan.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Order;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import com.itheima.utils.HandleResult;
import com.itheima.utils.SMSUtils;
import com.itheima.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Map;

/**
 * 套餐管理
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    private JedisPool jedisPool;



    /**
     * 发送手机验证码,存到redis,用于预约体检
     *
     * @return
     */
    @RequestMapping("/send4Order")
    public Result send4Order(String telephone) {
        //生成随机验证码,4位
        String code4String = ValidateCodeUtils.generateValidateCode(4).toString();
        try {
            //发送手机验证码
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, code4String);
        } catch (Exception e) {
            e.printStackTrace();
            return HandleResult.getResult(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        System.out.println("手机验证码为:" + code4String);
        //生成的验证码缓存到redis
//        jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_ORDER, 1*60, code4String);
//        jedisPool.getResource().sadd(telephone+ RedisMessageConstant.SENDTYPE_ORDER, code4String);
        return HandleResult.getResult(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

    /**
     * 发送验证码用于登录
     * @param telephone
     * @return
     */
    @RequestMapping("/send4Login")
    public Result send4Login(String telephone) {
        //生成随机验证码,4位
        String code4String = ValidateCodeUtils.generateValidateCode(4).toString();
        try {
            //发送手机验证码
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, code4String);
        } catch (Exception e) {
            e.printStackTrace();
            return HandleResult.getResult(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        System.out.println("手机登录登录验证码为:" + code4String);
        //生成的验证码缓存到redis
//        jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_LOGIN, 3*60, code4String);
//        jedisPool.getResource().sadd(telephone+ RedisMessageConstant.SENDTYPE_ORDER, code4String);
        return HandleResult.getResult(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
    /**
     * 发送验证码用于修改密码
     * @param telephone
     * @return
     */
    @RequestMapping("/send4ModifyPass")
    public Result send4ModifyPass(String telephone) {
        //生成随机验证码,4位
        String code4String = ValidateCodeUtils.generateValidateCode(4).toString();
        try {
            //发送手机验证码
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, code4String);
        } catch (Exception e) {
            e.printStackTrace();
            return HandleResult.getResult(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        System.out.println("手机登录登录验证码为:" + code4String);
        //生成的验证码缓存到redis
        jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_GETPWD, 3*60, code4String);
        return HandleResult.getResult(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }


}
