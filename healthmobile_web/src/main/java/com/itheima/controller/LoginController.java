package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constan.MessageConstant;
import com.itheima.constan.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.service.MemberSerivce;
import com.itheima.service.OrderService;
import com.itheima.service.SetmealService;
import com.itheima.utils.HandleResult;
import com.itheima.utils.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private MemberSerivce memberService;

    /**
     * 用通过手机验证码登录校验
     *
     * @param map
     * @return
     */
    @RequestMapping("/check")
    public Result submit(@RequestBody Map map, HttpServletResponse response) throws Exception {
        Result result = null;
        String telephone = (String) map.get("telephone");
        String validateCode = (String) map.get("validateCode");
        /*//获取redis中的验证码
        String redisValidateCode = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
        //判断用户输入的验证码和手机号是否为空
        if (validateCode == null || validateCode.equals("") || telephone == null || telephone.equals("")) {
            return HandleResult.getResult(false, MessageConstant.TELEPHONE_VALIDATECODE_NOTNULL);
        }

        //判断redis中的验证码是否已销毁清空
        if ((validateCode != null && !validateCode.equals("")) && (redisValidateCode == null || redisValidateCode == "")) {
            return HandleResult.getResult(false, MessageConstant.VALIDATECODE_OUTDATE);
        }
        //判断用户输入的验证码和redis中的验证码是否一致
        if ((validateCode != null && !validateCode.equals("")) && (!validateCode.equals(redisValidateCode))) {
            return HandleResult.getResult(false, MessageConstant.VALIDATECODE_ERROR);
        } else {*/
        //判断该手机号是否已经注册过会员
        Member member = memberService.findByTelephone(telephone);
        //如果注册过就直接返回Result
        if (member != null) {
            result = HandleResult.getResult(true, MessageConstant.LOGIN_SUCCESS);
        } else {
            //如果没有注册过,就用该手机号先注册会员,再返回result登录
            try {
                //当前用户不是会员，自动完成注册
                member = new Member();
                member.setPhoneNumber(telephone);
                member.setRegTime(new java.sql.Date(new Date().getTime()));
                memberService.add(member);
                result = HandleResult.getResult(true, MessageConstant.LOGIN_SUCCESS);
            } catch (Exception e) {
                e.printStackTrace();
                result = HandleResult.getResult(false, MessageConstant.ADD_MEMBER_FAIL);
            }
        }
        //用户信息保存到Cookie
        Cookie cookie = new Cookie("login_member_tephone", telephone);
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60);
        response.addCookie(cookie);
//        }
        return result;
    }
}
