package com.itheima.dao;

import com.itheima.pojo.Member;

import java.util.Date;
import java.util.List;

/**
 * 会员管理项
 */
public interface MemberDao {

    /**
     * 判断当前用户是否是会员
     * @param idCard
     * @return
     */
    Member isMemberByIdCard(String idCard) throws Exception;

    /**
     * 根据用户填写的预约信息,添加新的会员
     * @param member
     */
    void addMemberByMember(Member member) throws Exception;

    /**
     * 判断该手机号是否已经注册过会员
     * @param telephone
     * @return
     */
    Member isMemberByPhone(String telephone) throws Exception;
    /**
     * 注册新会员
     *
     * @param member
     * @throws Exception
     */
    void add(Member member) throws Exception;


    //今日新增会员数
    int getTodayNewMember(String today);
    //总会员数
    int getTotalMember();
    //本周新增会员数
    int getThisWeekNewMember(String firstDayOfWeek);
    //本月新增会员数
    Integer findMemberCountAfterDate(String firstDay4ThisMonth);


    /**
     * 统计男女会员数返回制作男女会员占比饼形图
     * @return
     */
    List<Integer> getSexCount() throws Exception;

    /**
     * 统计各年龄段会员数返回制作饼形图
     * @return
     * @throws Exception
     */
    List<Integer> getAllAge() throws Exception;
}
