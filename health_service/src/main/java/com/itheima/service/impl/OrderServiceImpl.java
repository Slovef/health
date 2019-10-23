package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.constan.MessageConstant;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.OrderSettingDao;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderService;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用户微信端预约套餐控制
 */
@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {


    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderSettingDao orderSettingDao;
    @Autowired
    private MemberDao memberDao;

    /**
     * 用户预约提交
     *
     * @param map
     * @return
     */
    public Result submitOder(Map map) throws Exception {

        //1.根据用户输入的预约日期判断t_ordersetting中有无当前日期的预约项
        //2.如果有当前日期的预约项,判断当前日期是否已预约满
        //3.如果没有预约满,判断当前用户t_member是否是会员,即根据telephoneNumber获得t_member,判断t_member是不是空既可
        //4.如果t_member不为空,即该用户已经是会员,判断该会员当前日期  当前套餐是否已经预约过,避免重复预约
        //5.如果t_member为空,就先注册该会员
        //6.然后t_order添加该用户的预约信息,新建预约信息时,orderStatus时未到诊状态
        //7.然后t_ordersetting当前预约项的预约人数+1

        Integer setmealId = Integer.parseInt(map.get("setmealId").toString());//套餐项
        String phoneNember = map.get("telephone").toString();//电话号
        String idCard = map.get("idCard").toString();//身份证
        String orderDate = map.get("orderDate").toString();//预约日期
        Date utilDate = DateUtils.parseString2Date(orderDate);
        //把java.util.Date转成数据库中java.sql.Date
        java.sql.Date date = new java.sql.Date(utilDate.getTime());
        //1.根据用户输入的预约日期判断t_ordersetting中有无当前日期的预约项
        OrderSetting orderSetting = orderSettingDao.findByDate(date);
        if (orderSetting == null) {
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        //2.如果有当前日期的预约项,判断当前日期是否已预约满
        if (orderSetting.getReservations() >= orderSetting.getNumber()) {
            return new Result(false, MessageConstant.ORDER_FULL);
        }
        //3.如果没有预约满,判断当前用户t_member是否是会员,即根据idCard获得t_member,判断t_member是不是空既可
        Member member = memberDao.isMemberByIdCard(idCard);
        //4.如果t_member不为空,即该用户已经是会员,判断该会员当前日期  当前套餐是否已经预约过,避免重复预约
        if (member != null) {
            Integer memberId = member.getId();
            Order order = new Order(null, memberId, date, null, null, setmealId);
            List<Order> isOrdered = orderDao.isOrdered(order);
            if (isOrdered != null && isOrdered.size() > 0) {
                //若果预约过,不进行重复预约
                return new Result(false, MessageConstant.HAS_ORDERED);
            }
        } else {
            //5.如果t_member为空,就先注册该会员
            String name = map.get("name").toString();
            String sex = map.get("sex").toString();
            member = new Member(null, null, name, sex, idCard, phoneNember, date, null, null, null, null);
            memberDao.addMemberByMember(member);
        }

        //没有预约过,建立新的用户预约order,t_ordersetting当前预约项的预约人数+1
        Integer memberId = member.getId();
        Order order = new Order(null, memberId, date, Order.ORDERTYPE_WEIXIN, Order.ORDERSTATUS_NO, setmealId);
        orderDao.addOrderByOrder(order);
        //t_ordersetting当前预约项的预约人数+1
        orderSetting.setReservations(orderSetting.getReservations() + 1);
        orderSettingDao.addOneInReservations(orderSetting);
        return new Result(true, MessageConstant.ORDER_SUCCESS, order.getId());
    }

    /**
     * 根据id查询预约Order
     *
     * @param id
     * @return
     */
    public Map findById(Integer id) throws Exception {
        Map map = orderDao.findById(id);
        //处理日期格式
        Date orderDate = (Date) map.get("orderDate");
        map.put("orderDate", DateUtils.parseDate2String(orderDate));
        return map;
    }


    /**
     * 各套餐预约占比饼形图
     * @return
     */
    public List<Map> getCountOfSetmeal() {
        List<Map> setmealCountList = orderDao.getCountOfSetmeal();
        System.out.println(setmealCountList);
        return setmealCountList;
    }


}
