package com.example.handler;

import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import org.apache.poi.ss.usermodel.Cell;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AutoColumnWidthHandler implements CellWriteHandler {

    // 每列最大宽度缓存
    private final Map<Integer, Integer> columnMaxWidthMap = new HashMap<>();

    @Override
    public void afterCellDispose(WriteSheetHolder sheetHolder,
                                 WriteTableHolder tableHolder,
                                 List<WriteCellData<?>> cellDataList,
                                 Cell cell,
                                 Head head,
                                 Integer relativeRowIndex,
                                 Boolean isHead) {

        if (cell == null) return;

        int columnIndex = cell.getColumnIndex();

        String cellValue = cell.toString();
        if (cellValue == null) return;

        // ⭐ 支持换行（按最长一行计算）
        int maxLineLength = 0;
        String[] lines = cellValue.split("\n");
        for (String line : lines) {
            int length = getStringLength(line);
            maxLineLength = Math.max(maxLineLength, length);
        }

        // Excel 列宽单位 = 字符数 * 256
        int columnWidth = maxLineLength * 256 + 500;

        // 限制最大宽度（防止过宽）
        columnWidth = Math.min(columnWidth, 255 * 256);

        Integer maxWidth = columnMaxWidthMap.get(columnIndex);
        if (maxWidth == null || columnWidth > maxWidth) {
            columnMaxWidthMap.put(columnIndex, columnWidth);
            sheetHolder.getSheet().setColumnWidth(columnIndex, columnWidth);
        }
    }

    /**
     * 中文算2个字符，英文算1个
     */
    private int getStringLength(String str) {
        int length = 0;
        for (char c : str.toCharArray()) {
            if (String.valueOf(c).getBytes().length > 1) {
                length += 2;
            } else {
                length += 1;
            }
        }
        return length;
    }
}