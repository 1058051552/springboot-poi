package com.example.entity;

import lombok.Data;
import java.util.List;

@Data
public class ExportRequest {
    private List<String> title;                  // 表头列表
    private List<List<Object>> values;           // 数据行列表
    private Position position;                   // 对齐方式配置

    @Data
    public static class Position {
        private List<String> titlePosition;      // 表头每列对齐方式，长度与title相同
        private List<String> valuesPosition;     // 数据每列对齐方式，长度与title相同
    }
}