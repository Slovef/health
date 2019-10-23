package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constan.MemberConstant;
import com.itheima.constan.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.service.OrderService;
import com.itheima.service.ReportService;
import com.itheima.utils.DateUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 数据统计
 */
@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    private ReportService reportService;

    @Reference
    private OrderService orderService;

    /**
     * 每月会员数量折线统计图
     *
     * @return
     */
    @RequestMapping("/getMemberReport")
    public Result getMemberReport() {
        try {
            //创建一个基于当前时间的日历对象,获得12个月前的日期
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -12);

            //创建集合,添加每一个月到集合中去
            List<String> list = new ArrayList<String>();
            for (int count = 0; count < 12; count++) {
                calendar.add(Calendar.MONTH, 1);
                list.add(new SimpleDateFormat("yyyy.MM").format(calendar.getTime()));
            }
            //创建集合,调用service,传入月份的List,得到每一个月份中会员的数量添加到集合中
            List<Integer> memeberList = new ArrayList<Integer>();
            memeberList = reportService.getCountByDate(list);
            //创建Map,添加月份的list和会员数量的List添加到map中
            Map map = new HashMap();
            map.put("months", list);
            map.put("memberCount", memeberList);
            return HandleResult.getResult(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return HandleResult.getResult(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }

    /**
     * 各套餐预约占比饼形图
     *
     * @return
     */
    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport() {
        Map reportMap = new HashMap();
        List<Map> setmealCountList = orderService.getCountOfSetmeal();
        List<String> setmealNameList = new ArrayList<String>();
        if (setmealCountList != null && setmealCountList.size() > 0) {
            for (Map map : setmealCountList) {
                setmealNameList.add((String) map.get("name"));
            }
        }
        reportMap.put("setmealNames", setmealNameList);
        reportMap.put("setmealCount", setmealCountList);
        return HandleResult.getResult(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, reportMap);
    }

    /**
     * 运营数据统计
     *
     * @return
     */
    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData() {
        Map<String, Object> result = null;
        try {
            result = reportService.getBussinessReport();
        } catch (Exception e) {
            e.printStackTrace();
            return HandleResult.getResult(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
        return HandleResult.getResult(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS, result);
    }

    /**
     * 导出Excel报表
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response) {
        try {
            //远程调用报表服务获得报表数据
            Map<String, Object> result = reportService.getBussinessReport();
            //获取返回的结果数据准备写入到Excel文件中
            String reportDate = (String) result.get("reportDate");
            Integer todayNewMember = (Integer) result.get("todayNewMember");
            Integer totalMember = (Integer) result.get("totalMember");
            Integer thisWeekNewMember = (Integer) result.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) result.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) result.get("todayOrderNumber");
            Integer thisWeekOrderNumber = (Integer) result.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer) result.get("thisMonthOrderNumber");
            Integer todayVisitsNumber = (Integer) result.get("todayVisitsNumber");
            Integer thisWeekVisitsNumber = (Integer) result.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer) result.get("thisMonthVisitsNumber");
            List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");
            //获得Excel模板文件的绝对路径
            String templatePath = request.getSession().getServletContext().getRealPath("template") + File.separator + "report_template.xlsx";
            //读取模板文件创建Excel表格对象
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File(templatePath)));
            //获得Excel的第一页
            XSSFSheet sheet = workbook.getSheetAt(0);
            //把结果数据填写到Excel中
            XSSFRow row = sheet.getRow(2);
            row.getCell(5).setCellValue(reportDate);//日期

            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);//新增会员数（本日）
            row.getCell(7).setCellValue(totalMember);//总会员数

            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber);//今日到诊数

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周到诊数

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月到诊数
            int rowNum = 12;
            for (Map map : hotSetmeal) {
                String name = (String) map.get("name");
                Long setmeal_count = (Long) map.get("setmeal_count");
                BigDecimal proportion = (BigDecimal) map.get("proportion");
                String remark = (String) map.get("remark");
                row = sheet.getRow(rowNum++);
                row.getCell(4).setCellValue(name);//套餐名称
                row.getCell(5).setCellValue(setmeal_count);//预约数量
                row.getCell(6).setCellValue(proportion.doubleValue());//占比
                row.getCell(7).setCellValue(remark);//备注
            }
            //通过输出流进行文件下载
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");
            workbook.write(outputStream);

            outputStream.flush();
            outputStream.close();
            workbook.close();

            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return HandleResult.getResult(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

    /**
     * 根据用户输入的日期得到每月会员数绘制折线图
     *
     * @param date
     * @return
     */
    @RequestMapping(value = "/getMemberReportByDate", method = RequestMethod.POST)
    public Result getMemberReportByDate(String date) {
        //得到的date格式需要转换:
        // Mon Jul 01 2019 00:00:00 GMT 0800 (中国标准时间),Thu Aug 08 2019 00:00:00 GMT 0800 (中国标准时间)
        Map map = new HashMap();
        String[] splitDate = date.split(",");
        try {
            //定义格式化模板
            SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd yyyy HH:mm:ss z", Locale.US);
            //把字符串格式转成Date格式
            Date formatBeginDate = sdf.parse(splitDate[0]);
            Date formatEndDate = sdf.parse(splitDate[1]);
            //调用工具获得指定的日期格式2019-07-02
            String beginDate = DateUtils.parseDate2String(formatBeginDate);
            String endDate = DateUtils.parseDate2String(formatEndDate);
            //调用工具得到月份列表[2019-07,2019-08]
            List<String> monthList = DateUtils.getMonthBetween(beginDate, endDate, "yyyy-MM");
            //调用reortService获得会员数集合
            List<Integer> memberList = reportService.getCountByDate(monthList);
            map.put("months", monthList);
            map.put("memberCount", memberList);
        } catch (Exception e) {
            e.printStackTrace();
            return HandleResult.getResult(false,MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
        return HandleResult.getResult(true,MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
    }

    /**
     * 统计男女会员数返回制作男女会员占比饼形图
     * @return
     */
    @RequestMapping("/getMemberSexReport")
    public Result getMemberSexReport() {
        try {
            Map resultMap = new HashMap();
            List<String> nameList = new ArrayList<String>();
            nameList.add(MemberConstant.NO_SEX_MEMBER_COUNT);
            nameList.add(MemberConstant.BOY_MEMBER_COUNT);
            nameList.add(MemberConstant.GIRL_MEMBER_COUNT);
            List<Integer> numberList = reportService.getCountOfSex();
            List<Map> countList = new ArrayList<Map>();
            int count = 0;
            for (String name : nameList) {
                Map map = new HashMap();
                map.put("name", name);
                map.put("value", numberList.get(count));
                countList.add(map);
                count++;
            }
            resultMap.put("memberNames", nameList);
            resultMap.put("memberCount", countList);
            return HandleResult.getResult(true,MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            return HandleResult.getResult(false,MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }

    /**
     * 统计各年龄段会员数返回制作饼形图
     * @return
     */
    @RequestMapping("/getMemberAgeReport")
    public Result getMemberAgeReport() {
        try {
            Map resultMap = new HashMap();
            List<Map> memberAgeCountList = reportService.getCountByAge();
            List<String> memberAgeNameList = new ArrayList<String>();
            Collections.addAll(
                    memberAgeNameList,
                    MemberConstant.ZERO_EIGHTEEN_COUTN,
                    MemberConstant.EIGHTEEN_THIRTY_COUTN,
                    MemberConstant.THIRTY_FORTY_FIVE_COUTN,
                    MemberConstant.FORTY_FIVE_UP_COUTN
            );
            resultMap.put("memberAgeName", memberAgeNameList);
            resultMap.put("memberAgeCount", memberAgeCountList);
            return HandleResult.getResult(true,MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            return HandleResult.getResult(false,MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }

}
