package com.itheima.test;

import com.itheima.constan.RedisConstant;
import com.itheima.constan.RedisMessageConstant;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

public class JedisTest {

    @Autowired
    private JedisPool jedisPool;

//    @Test
    public void test01() {
//        jedisPool.getResource().setex("17512070130" + RedisMessageConstant.SENDTYPE_ORDER, 1 * 30, "7777");
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES, "777777");

    }
}
