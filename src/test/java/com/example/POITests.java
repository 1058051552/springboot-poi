package com.example;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static org.apache.poi.ss.usermodel.CellType.STRING;

@SpringBootTest
class POITests {

    /**
     * 03版的excel支持最大65535的写出，多了会异常！
     */
    @Test
    void write_03() {
        LocalDateTime start = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String format = start.format(dtf);
        //1.创建一个工作薄
        Workbook workbook = new HSSFWorkbook();
        //2.创建一个工作表
        Sheet sheet = workbook.createSheet("我的健身计划表");
        //3.创建一个行
        Row row_0 = sheet.createRow(0);
        //4.创建一个单元格
        Cell cell_0_0 = row_0.createCell(0);
        cell_0_0.setCellValue("暴瘦20斤");
        Cell cell_0_1 = row_0.createCell(1);
        cell_0_1.setCellValue(format);

        Row row_2 = sheet.createRow(1);
        Cell cell_1_0 = row_2.createCell(0);
        cell_1_0.setCellValue("月瘦10斤");
        Cell cell_1_1 = row_2.createCell(1);
        cell_1_1.setCellValue(format);

        try( FileOutputStream fos = new FileOutputStream("D:\\file\\springboot-poi\\健身计划.xls")) {
            workbook.write(fos);
            LocalDateTime end = LocalDateTime.now();
            long seconds = Duration.between(start, end).getSeconds();
            System.out.println("03_总耗时："+seconds);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 07版和03版格式不一样
     * 07版的excel支持大数据量的写出，但是比较耗内存！
     * 批量导出
     */
    @Test
    void write_07() {

        LocalDateTime start = LocalDateTime.now();
        //1.创建一个工作薄
        Workbook workbook = new XSSFWorkbook();
        //2.创建一个工作表
        Sheet sheet = workbook.createSheet("我的健身计划表");
        //3.创建一个行
        for (int i = 0; i < 100000; i++) {
            Row row = sheet.createRow(i);
            for (int j = 0; j < 10; j++) {
                Cell cell = row.createCell(j);
                cell.setCellValue(j);
            }
        }

        try( FileOutputStream fos = new FileOutputStream("D:\\file\\springboot-poi\\健身计划.xlsx")) {
            workbook.write(fos);
            LocalDateTime end = LocalDateTime.now();
            long seconds = Duration.between(start, end).getSeconds();
            System.out.println("07_总耗时："+seconds);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 07版和03版格式不一样
     * 07版的excel支持大数据量的写出，但是比较耗内存！
     * 批量导出(加强版)默认读取100条数据
     */
    @Test
    void write_07_plus() {
        LocalDateTime start = LocalDateTime.now();
        Workbook sheets = new SXSSFWorkbook();
        Sheet sheet = sheets.createSheet("健身计划规划表");

        for (int i = 0; i < 10_0000; i++) {
            Row row = sheet.createRow(i);
            for (int j = 0; j < 10; j++){
                Cell cell = row.createCell(j);
                cell.setCellValue("我想暴瘦！");
            }
        }

        try (FileOutputStream fos = new FileOutputStream("D:\\file\\springboot-poi\\健身计划.xlsx")){
            sheets.write(fos);
            LocalDateTime end = LocalDateTime.now();
            long seconds = Duration.between(start, end).getSeconds();
            System.out.println("加强版耗时："+seconds);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //释放缓存，只有使用加强版的时候才会需要关闭
            ((SXSSFWorkbook)sheets).dispose();
        }
    }

    @Test
    public void Test_Read_03(){
        String PATH = "C:\\Users\\Administrator.FYYX-2019BPEWMU\\Desktop\\商品信息.xls";
        try(FileInputStream fis = new FileInputStream(PATH)) {
            //1.创建一个工作薄
            Workbook workbook = new HSSFWorkbook(fis);
            //2.得到表
            Sheet sheet = workbook.getSheetAt(0);
            //3.得到行
            Row rowtitle = sheet.getRow(0);
            if(rowtitle!=null){
                //表中一行的长度
                int cells = rowtitle.getPhysicalNumberOfCells();
                for (int i = 0; i < cells; i++) {
                    Cell cell = rowtitle.getCell(i);
                    if(cell!=null){
                        CellType cellType = cell.getCellType();
                        String value = cell.getStringCellValue();
                        System.out.print(value+"|");
                    }
                }
            }

            //获取表中的内容
            int rows = sheet.getPhysicalNumberOfRows();
            for (int i = 0; i < rows; i++) {
                Row row = sheet.getRow(i);
                if(row!=null){
                    //读取列
                    int number = row.getPhysicalNumberOfCells();
                    for (int j = 0; j < number; j++) {
                        Cell cell = row.getCell(j);
                        if(cell!=null){
                            CellType cellType = cell.getCellTypeEnum();
                            switch (cellType){
                                case STRING: //字符串
                                    System.out.println("String");
                                    System.out.println(cell.getStringCellValue());
                                    break;
                                case BOOLEAN: //布尔
                                    System.out.println("BOOLEAN");
                                    System.out.println(cell.getBooleanCellValue());
                                    break;
                                case ERROR: //错误的格式
                                    System.out.println("ERROR");
                                    System.out.println(cell.getErrorCellValue());
                                    break;
                                case NUMERIC://数字(日期、普通数字)
                                    System.out.println("NUMERIC");
                                    if(HSSFDateUtil.isCellDateFormatted(cell)){
                                        Date date = cell.getDateCellValue();
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        String format = sdf.format(date);
                                        System.out.println(format);
                                    }else{
                                        //如果不是日期格式防止数字太长，转换为字符串输出
                                        cell.setCellType(STRING);
                                        System.out.println(cell.toString());
                                    }
                                    break;
                                case BLANK:
                                    System.out.println("BLANK");
                                    break;

                            }
                        }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
