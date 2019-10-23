package com.itheima.test;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class CreateExcelTest {
    public static void main(String[] args) throws Exception {
        //创建工作簿对象
        XSSFWorkbook workbook = new XSSFWorkbook();
        //创建工作表对象
        XSSFSheet xssfSheet = workbook.createSheet("学生表");
        //创建行
        XSSFRow row01 = xssfSheet.createRow(0);
        //创建列添加内容
        row01.createCell(0).setCellValue("姓名");
        row01.createCell(1).setCellValue("性别");
        row01.createCell(2).setCellValue("地址");

        XSSFRow row02 = xssfSheet.createRow(1);
        row02.createCell(0).setCellValue("张三");
        row02.createCell(1).setCellValue("男");
        row02.createCell(2).setCellValue("深圳");

        XSSFRow row03 = xssfSheet.createRow(2);
        row03.createCell(0).setCellValue("李四");
        row03.createCell(1).setCellValue("男");
        row03.createCell(2).setCellValue("北京");

        //通过输出流输出到本地
        FileOutputStream os = new FileOutputStream("G:/student.xlsx");
        workbook.write(os);
        os.flush();
        os.close();
        workbook.close();
    }
}
