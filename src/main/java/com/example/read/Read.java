package com.example.read;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.example.entity.User;

import java.util.*;

public class Read {
    public static void main(String[] args) {
        // 有个很重要的点 DemoDataListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
        // 写法1：
        String fileName = "user.xls";

        List<User> list = new ArrayList<User>(Arrays.asList(
                new User(10000,"赵柳","男",6000.00,new Date(),""),
                new User(10001,"王五","男",7000.00,new Date(),""),
                new User(10002,"小钱","男",8000.00,new Date(),""),
                new User(10003,"小刘","女",5500.00,new Date(),""),
                new User(10004,"晓晓","女",5200.00,new Date(),""),
                new User(10005,"菲菲","女",5300.00,new Date(),"")
        ));


   /*   //剔除掉不需要打印的字段
        Set<String> set = new HashSet<>();
        set.add("sex");
        set.add("salary");
        // 这里需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        EasyExcel.write(fileName, User.class).excludeColumnFiledNames(set).sheet("用户信息").doWrite(list);
   */

        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        EasyExcel.write(fileName, User.class).sheet("用户信息").doWrite(list);



        // 写法2
        fileName =  "user_"+System.currentTimeMillis() + ".xlsx";
        // 这里 需要指定写用哪个class去写
        ExcelWriter excelWriter = EasyExcel.write(fileName, User.class).build();
        WriteSheet writeSheet = EasyExcel.writerSheet("用户信息").build();
        excelWriter.write(list, writeSheet);
        /// 千万别忘记finish 会帮忙关闭流
        excelWriter.finish();




    }
}
