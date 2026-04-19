package com.example.service;

import com.alibaba.excel.EasyExcel;
import com.example.entity.ExportRequest;
import com.example.handler.AutoColumnWidthHandler;
import com.example.handler.CustomCellStyleStrategy;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ExcelExportService {

    /**
     * 导出Excel到HTTP响应流
     *
     * @param request  导出参数
     * @param response HttpServletResponse
     */
    public void export(ExportRequest request, HttpServletResponse response) throws IOException {
        String fileName = URLEncoder.encode("测试导出", "UTF-8");

        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

        EasyExcel.write(response.getOutputStream())
                 .useDefaultStyle(false)  // ⭐ 必须关掉默认样式
                 .head(buildHead(request.getTitle()))
                 .registerWriteHandler(new CustomCellStyleStrategy(
                         request.getPosition().getTitlePosition(),
                         request.getPosition().getValuesPosition()
                 ))
                 .registerWriteHandler(new AutoColumnWidthHandler()) // ⭐ 自动列宽
                 .sheet("sheet1")
                 .doWrite(request.getValues());
    }

    private List<List<String>> buildHead(List<String> titles) {
        List<List<String>> head = new ArrayList<>();
        for (String t : titles) {
            head.add(Collections.singletonList(t));
        }
        return head;
    }
}