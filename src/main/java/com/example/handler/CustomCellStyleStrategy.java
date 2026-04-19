package com.example.handler;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.style.AbstractCellStyleStrategy;
import org.apache.poi.ss.usermodel.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomCellStyleStrategy extends AbstractCellStyleStrategy {

    private List<String> titlePosition;
    private List<String> valuesPosition;

    // 样式缓存（必须）
    private Map<String, CellStyle> styleCache = new HashMap<>();

    public CustomCellStyleStrategy(List<String> titlePosition, List<String> valuesPosition) {
        this.titlePosition = titlePosition;
        this.valuesPosition = valuesPosition;
    }

    @Override
    protected void setHeadCellStyle(Cell cell, Head head, Integer relativeRowIndex) {
        int colIndex = cell.getColumnIndex();
        String pos = titlePosition.get(colIndex);

        CellStyle style = getOrCreateStyle(cell, "head_" + pos, pos);
        cell.setCellStyle(style);
    }

    @Override
    protected void setContentCellStyle(Cell cell, Head head, Integer relativeRowIndex) {
        int colIndex = cell.getColumnIndex();
        String pos = valuesPosition.get(colIndex);

        CellStyle style = getOrCreateStyle(cell, "data_" + pos, pos);
        cell.setCellStyle(style);
    }

    private CellStyle getOrCreateStyle(Cell cell, String key, String pos) {
        if (styleCache.containsKey(key)) {
            return styleCache.get(key);
        }

        Workbook workbook = cell.getSheet().getWorkbook();

        CellStyle style = workbook.createCellStyle();

        // ⭐ 关键：继承默认样式（否则表头会丢样式）
        style.cloneStyleFrom(cell.getCellStyle());

        style.setAlignment(getAlignment(pos));

        // 自动换行（你有 \n）
        style.setWrapText(true);

        // ⭐⭐⭐ 表头加背景色
        if (key.startsWith("head_")) {
            style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // 可选：加粗（如果你关闭了默认样式）
            Font font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);
        }

        styleCache.put(key, style);
        return style;
    }

    private HorizontalAlignment getAlignment(String pos) {
        if ("left".equalsIgnoreCase(pos)) {
            return HorizontalAlignment.LEFT;
        } else if ("right".equalsIgnoreCase(pos)) {
            return HorizontalAlignment.RIGHT;
        } else {
            return HorizontalAlignment.CENTER;
        }
    }
}