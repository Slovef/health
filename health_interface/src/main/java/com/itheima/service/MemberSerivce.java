package com.itheima.service;

import com.itheima.pojo.Member;

import java.util.List;

/**
 * 会员服务接口层
 */
public interface MemberSerivce{
    /**
     * 根据手机号码查询会员信息
     * @param telephone
     * @return
     */
    Member findByTelephone(String telephone) throws Exception;

    /**
     * 注册会员
     * @param member
     */
    void add(Member member) throws Exception;

    /**
     * 根据月份查询 会员数量
     * @param months
     * @return
     */
    List<Integer> findMemberCountByMonth(List<String> months) throws Exception;
}
