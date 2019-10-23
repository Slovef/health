package com.itheima.test;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;

public class POITest {
    public static void main(String[] args) throws Exception {
        //创建工作簿对象
        XSSFWorkbook workbook = new XSSFWorkbook("G:\\javalx\\health_parent\\health_jobs\\src\\main\\resources\\read.xlsx");
        //获得工作表对象
        XSSFSheet sheet = workbook.getSheetAt(0);
        //遍历工作表获得行对象
        for (Row row : sheet) {
            //遍历工作表获得列对象
            for (Cell cell : row) {
                //获得列里面的内容
                System.out.println(cell.getStringCellValue());
            }
            System.out.println("-------------------");
        }
        //关闭
        workbook.close();
    }

}
