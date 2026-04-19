package com.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.entity.ExportRequest;
import com.example.entity.OutEvalTopic;
import com.example.service.ExcelExportService;
import com.example.util.EasyExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Email:1058051552@qq.com
 * Date 2022-7-21 23:04
 * Description 代码版权即所在公司及个人所有
 *
 * @author tianshaofei
 */
@RestController
public class FileController {

    @RequestMapping("/importExcel")
    public Object importExcelSubject(MultipartFile file) throws IOException {
        List<OutEvalTopic> list = EasyExcelUtil.read(file.getInputStream(),
                OutEvalTopic.class);
        return list;
    }


    @Autowired
    private ExcelExportService exportService;

    @PostMapping("/export/excel")
    public void exportExcel(HttpServletResponse response) throws IOException {
        String str = "{\n" +
                     "    \"title\": [\"客户\",\"单号\",\"创建者\",\"money\",\"更新时间\",\"更新时间1\"],\n" +
                     "    \"values\": [\n" +
                     "        [\"我是第一条的客户名称 哈哈哈 我是带有杠嗯的内容\",\"mi\",null,\"但是但是犯得上\",\"2023-01-02 9:01:22\",\"2023-01-02 9:01:56\"],\n" +
                     "        [\"我有7个br是第2条的客户\\n名称哈哈哈\\n我是\\n带有bir的\\n\\n\\n内容\",null,null,\"犯得上犯得上\",\"2023-01-02 9:01:29\",\"2023-01-02 9:01:28\"]\n" +
                     "    ],\n" +
                     "    \"position\": {\n" +
                     "        \"titlePosition\": [\"right\",\"left\",\"left\",\"left\",\"left\",\"left\"],\n" +
                     "        \"valuesPosition\": [\"right\",\"right\",\"left\",\"left\",\"left\",\"left\"]\n" +
                     "    }\n" +
                     "}";
        ExportRequest exportRequest = JSONObject.parseObject(str, ExportRequest.class);
        exportService.export(exportRequest, response);
    }
}
