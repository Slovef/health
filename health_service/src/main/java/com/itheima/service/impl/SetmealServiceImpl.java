package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import org.codehaus.jackson.map.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constan.RedisConstant;
import com.itheima.dao.SetmealDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Member;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.type.TypeReference;

/**
 * 业务层,套餐管理
 * interfaceClass:使用的Cglib产生的代理对象的类型是HelloService类型
 */
@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealDao setmealDao;
    @Autowired
    private JedisPool jedisPool;


    /**
     * 查询所有的检查组返回给新增检查组显示
     *
     * @return
     */
    public List<CheckGroup> findAllCheckGroup() throws Exception {
        List<CheckGroup> checkGroups = setmealDao.findAllCheckGroup();
        return checkGroups;
    }

    /**
     * 添加套餐
     *
     * @param setmeal,checkgroupIds
     * @return
     */
    public void addSetmeal(Setmeal setmeal, int[] checkgroupIds) throws Exception {
        //新增套餐信息
        setmealDao.addSetmeal(setmeal);
        //System.out.println(checkGroup.getId());
        Integer setmealId = setmeal.getId();
        //新增中间表
        addSetmeal_CheckGroup(setmealId, checkgroupIds);
    }

    //新增中间表方法
    public void addSetmeal_CheckGroup(Integer setmealId, int[] checkgroupIds) throws Exception {
        if (checkgroupIds != null && checkgroupIds.length > 0) {
            for (int checkgroupId : checkgroupIds) {
                Map<String, Integer> map = new HashMap<String, Integer>();
                map.put("setmeal_id", setmealId);
                map.put("checkgroup_id", checkgroupId);
                setmealDao.addSetmeal_CheckGroup(map);
            }
        }
    }


    /**
     * 分页查询,填充页面
     *
     * @param queryPageBean
     * @return
     */
    public PageResult queryPage(QueryPageBean queryPageBean) throws Exception {

        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        Page<Setmeal> pageResult = setmealDao.queryPage(queryPageBean.getQueryString());
        long total = pageResult.getTotal();
        List<Setmeal> result = pageResult.getResult();
        return new PageResult(total, result);
    }

    /**
     * 查询所有的套餐项(Redis有的话就从Redis中获取没有的家就从myasql中获取)
     *
     * @return
     */
    public List<Setmeal> findAllSetmeal() throws Exception {
        String data = null;
        Jedis jedis = jedisPool.getResource();
        //从redis中查询
        data = getfromRedis(jedis, RedisConstant.SETMEAL_LIST_DB_RESOURCES);
        if (data == null) {
            //从Mysql中查
            data = getListFromMysql();
            //查询到以后存到Redis中
            saveDataToRedis(data, jedis, RedisConstant.SETMEAL_LIST_DB_RESOURCES);
        } else {
            System.out.println("该套餐列表数据是从Redis中查询得到....");
        }

        //json字符串转对象
        ObjectMapper objectMapper = new ObjectMapper();
        List<Setmeal> setmealList = objectMapper.readValue(data, new TypeReference<List<Setmeal>>() {});
        return setmealList;
    }

    //从redis中查询数据
    public String getfromRedis(Jedis jedis, String key) {
        if (jedis != null) {
            return jedis.get(key);
        }
        return null;
    }

    //从数据库中查询
    //查询列表
    public String getListFromMysql() throws Exception {
        List<Setmeal> allSetmeal = setmealDao.findAllSetmeal();
        //list转json
        ObjectMapper objectMapper = new ObjectMapper();
        //把list转成json返回
        String data = objectMapper.writeValueAsString(allSetmeal);
        return data;
    }

    //查询详情
    public String getDetailFromMysql(int id) throws Exception {
        Setmeal setmeal = setmealDao.findById(id);
        //list转json
        ObjectMapper objectMapper = new ObjectMapper();
        //把list转成json返回
        String data = objectMapper.writeValueAsString(setmeal);
        return data;
    }

    //把数据库查询到的List存到redis中
    public void saveDataToRedis(String data, Jedis jedis, String key) {
        if (jedis != null) {
            jedis.set(key, data);
        }
    }


    /**
     * 点击展示套餐详情
     *
     * @param id
     * @return
     */
    public Setmeal findById(int id) throws Exception {
        String data = null;
        Jedis jedis = jedisPool.getResource();
        //从redis中查询
        data = getfromRedis(jedis, RedisConstant.SETMEAL_DETAIL_DB_RESOURCES);
        if (data == null) {
            //从Mysql中查
            data = getDetailFromMysql(id);
            //查询到以后存到Redis中
            saveDataToRedis(data, jedis, RedisConstant.SETMEAL_DETAIL_DB_RESOURCES);
        } else {
            System.out.println("该套餐详情数据是从Redis中查询得到....");
        }

        //json字符串转对象
        ObjectMapper objectMapper = new ObjectMapper();
//        List<Setmeal> setmealList = objectMapper.readValue(data, new TypeReference<List<Setmeal>>() {});
        Setmeal setmeal = objectMapper.readValue(data, Setmeal.class);
        return setmeal;

    }

}
