package com.itheima.dao;

import java.util.Map;

/**
 * 统计每个月会员数量
 */
public interface ReportDao {
    /**
     * 统计每个月会员数量
     * @param map
     * @return
     */
    Integer getCountByDate(Map map);
}
