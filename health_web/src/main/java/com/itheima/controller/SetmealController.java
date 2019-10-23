package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constan.MessageConstant;
import com.itheima.constan.RedisConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Setmeal;
import com.itheima.service.CheckgroupService;
import com.itheima.service.SetmealService;
import com.itheima.utils.QiniuUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 套餐管理
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    private SetmealService setmealService;

    @Autowired
    private JedisPool jedisPool;

    /**
     * 查询所有的检组返回给新增套餐显示
     * @return Result
     */
    @RequestMapping(value = "/findAllCheckGroup",method = RequestMethod.GET)
    public Result findAllCheckGroup() {
        try {
            List<CheckGroup> checkGroups = setmealService.findAllCheckGroup();
            return HandleResult.getResult(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroups);
        } catch (Exception e) {
            e.printStackTrace();
            return HandleResult.getResult(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

    /**
     * 添加套餐
     * @param setmeal,checkgroupIds
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result add(@RequestBody Setmeal setmeal, int[] checkgroupIds) {
        try {
            setmealService.addSetmeal(setmeal,checkgroupIds);
            //当用户点击确定以后,redis记录此数据,用于以后垃圾处理
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, setmeal.getImg());
            return HandleResult.getResult(true, MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return HandleResult.getResult(false, MessageConstant.ADD_SETMEAL_FAIL);
        }
    }

    /**
     * 分页查询,填充页面
     * @param queryPageBean
     * @return
     */
    @RequestMapping(value = "/queryPage", method = RequestMethod.POST)
    public Result queryPage(@RequestBody QueryPageBean queryPageBean) {
        try {
            PageResult pageResult = setmealService.queryPage(queryPageBean);
            return HandleResult.getResult(true, MessageConstant.QUERY_SETMEALLIST_SUCCESS,pageResult);
        } catch (Exception e) {
            e.printStackTrace();
            return HandleResult.getResult(false, MessageConstant.QUERY_SETMEALLIST_FAIL);
        }
    }

    /**
     * 图片上传
     *
     */

    @RequestMapping("/upload")
    public Result upload(@RequestParam MultipartFile imgFile) {
        try {
            //获取原始文件名
            String originalFilename = imgFile.getOriginalFilename();
            //截取后缀
            String imgName = originalFilename.substring(originalFilename.lastIndexOf("."));
            //生成随机文件名+后缀
            String newImgName = UUID.randomUUID().toString() + imgName;
            //使用七牛云工具类做上传
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),newImgName);
            //用户选择图片上传后,需要redis第一次记录此数据便于以后做垃圾处理
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES, newImgName);
            //文件名一定要返回
            return HandleResult.getResult(true, MessageConstant.PIC_UPLOAD_SUCCESS, newImgName);
        } catch (IOException e) {
            e.printStackTrace();
            return HandleResult.getResult(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }



}
