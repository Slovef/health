package com.itheima.jobs;

import com.itheima.constan.RedisConstant;
import com.itheima.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

public class CleanImgJob {

    @Autowired
    private JedisPool jedisPool;

    public void clearImgs() {
        System.out.println("清理图片任务执行了.......");
        Set<String> sdiff = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        if (sdiff != null && sdiff.size() > 0) {
            for (String pic : sdiff) {
                QiniuUtils.deleteFileFromQiniu(pic);
                System.out.println("七牛云中的垃圾图片被清理了");
                jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES, pic);
                System.out.println("redis中的垃圾图片被清理了");
            }
        }

    }
}
