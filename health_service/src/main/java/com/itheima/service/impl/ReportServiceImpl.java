package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.constan.MemberConstant;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.ReportDao;
import com.itheima.service.OrderService;
import com.itheima.service.ReportService;
import com.itheima.service.SetmealService;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportDao reportDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;


    /**
     * 统计每个月会员数量
     *
     * @param list
     * @return
     */
    public List<Integer> getCountByDate(List<String> list) {
        List<Integer> memberList = new ArrayList();
        if (list != null && list.size() > 0) {
            for (String regTime : list) {
                String beginDate = regTime + ".01";
                String endDate = regTime + ".31";
                Map map = new HashMap();
                map.put("beginDate", beginDate);
                map.put("endDate", endDate);
                Integer count = reportDao.getCountByDate(map);
                memberList.add(count);
            }
        }
        return memberList;
    }

    /**
     * 运营数据统计
     *
     * @return
     */
    public Map<String, Object> getBussinessReport() throws Exception {


        Map<String, Object> map = new HashMap<String, Object>();
        //今日日期  reportDate
        String today = DateUtils.parseDate2String(DateUtils.getToday());
        //获得本周一的日期
        String thisWeekMonday = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());
        //获得本月第一天的日期
        String firstDay4ThisMonth = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());
        //今日新增会员数
        int todayNewMember = memberDao.getTodayNewMember(today);
        //总会员数
        int totalMember = memberDao.getTotalMember();
        //本周新增会员数
        int thisWeekNewMember = memberDao.getThisWeekNewMember(thisWeekMonday);
        //本月新增会员数
        Integer thisMonthNewMember = memberDao.findMemberCountAfterDate(firstDay4ThisMonth);
        //今日预约数
        Integer todayOrderNumber = orderDao.findOrderCountByDate(today);
        //本周预约数
        Integer thisWeekOrderNumber = orderDao.findOrderCountAfterDate(thisWeekMonday);
        //本月预约数
        Integer thisMonthOrderNumber = orderDao.findOrderCountAfterDate(firstDay4ThisMonth);
        //今日到诊数
        Integer todayVisitsNumber = orderDao.findVisitsCountByDate(today);
        //本周到诊数
        Integer thisWeekVisitsNumber = orderDao.findVisitsCountAfterDate(thisWeekMonday);
        //本月到诊数
        Integer thisMonthVisitsNumber = orderDao.findVisitsCountAfterDate(firstDay4ThisMonth);
        //热门套餐（取前4）*/
        /*"hotSetmeal":[
      {"proportion":0.4545,"name":"粉红珍爱(女)升级TM12项筛查体检套餐","setmeal_count":5},
      {"proportion":0.1818,"name":"阳光爸妈升级肿瘤12项筛查体检套餐","setmeal_count":2},
      {"proportion":0.1818,"name":"珍爱高端升级肿瘤12项筛查","setmeal_count":2},
      {"proportion":0.0909,"name":"孕前检查套餐","setmeal_count":1}
    ],*/
        List<Map> hotSetmealList = orderDao.getHotSetmealList();

        map.put("reportDate", today);
        map.put("todayNewMember", todayNewMember);
        map.put("totalMember", totalMember);
        map.put("thisWeekNewMember", thisWeekNewMember);
        map.put("thisMonthNewMember", thisMonthNewMember);
        map.put("todayOrderNumber", todayOrderNumber);
        map.put("thisWeekOrderNumber", thisWeekOrderNumber);
        map.put("thisMonthOrderNumber", thisMonthOrderNumber);
        map.put("todayVisitsNumber", todayVisitsNumber);
        map.put("thisWeekVisitsNumber", thisWeekVisitsNumber);
        map.put("thisMonthVisitsNumber", thisMonthVisitsNumber);
        map.put("hotSetmeal", hotSetmealList);

        return map;
    }

    /**
     * 统计男女会员数返回制作男女会员占比饼形图
     *
     * @return
     */
    public List<Integer> getCountOfSex() throws Exception {
        return memberDao.getSexCount();
    }

    /**
     * 统计各年龄段会员数返回制作饼形图
     *
     * @return
     * @throws Exception
     */
    public List<Map> getCountByAge() throws Exception {
        List<Integer> ageList = memberDao.getAllAge();
        Integer count1 = 0;
        Integer count2 = 0;
        Integer count3 = 0;
        Integer count4 = 0;
        for (Integer age : ageList) {
            if (age > 0 && age <= 18) {
                count1++;
            }
            if (age > 18 && age <= 30) {
                count2++;
            }
            if (age > 30 && age <= 45) {
                count3++;
            }
            if (age > 45) {
                count4++;
            }
        }
        Map map1 = new HashMap();
        Map map2 = new HashMap();
        Map map3 = new HashMap();
        Map map4 = new HashMap();
        map1.put("name", MemberConstant.ZERO_EIGHTEEN_COUTN);
        map1.put("value", count1);
        map2.put("name", MemberConstant.EIGHTEEN_THIRTY_COUTN);
        map2.put("value", count2);
        map3.put("name", MemberConstant.THIRTY_FORTY_FIVE_COUTN);
        map3.put("value", count3);
        map4.put("name", MemberConstant.FORTY_FIVE_UP_COUTN);
        map4.put("value", count4);
        List<Map> resultList = new ArrayList<Map>();
        Collections.addAll(resultList, map1, map2, map3, map4);
        return resultList;
    }
}
