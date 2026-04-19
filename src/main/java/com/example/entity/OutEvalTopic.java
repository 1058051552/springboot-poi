package com.example.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * Email:1058051552@qq.com
 * Date 2022-7-21 23:03
 * Description 代码版权即所在公司及个人所有
 *
 * @author tianshaofei
 */
@Data
public class OutEvalTopic {
    @ExcelProperty(value = "课题类别")
    private String type;

    @ExcelProperty(value = "课题名称")
    private String name;



}
