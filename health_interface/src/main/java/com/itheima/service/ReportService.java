package com.itheima.service;

import java.util.List;
import java.util.Map;

/**
 * 统计每个月会员数量
 */
public interface ReportService {
    /**
     * 统计每个月会员数量
     * @param list
     * @return
     */
    List<Integer> getCountByDate(List<String> list);
    /**
     * 运营数据统计
     *
     * @return
     */
    Map<String, Object> getBussinessReport() throws Exception;

    /**
     * 统计男女会员数返回制作男女会员占比饼形图
     * @return
     */
    List<Integer> getCountOfSex() throws Exception;

    /**
     * 统计各年龄段会员数返回制作饼形图
     * @return
     */
    List<Map> getCountByAge() throws Exception;
}
