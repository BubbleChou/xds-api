package com.bubble.xds.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhoujingbo/Bob
 * @version 1.0
 * @date 2020/8/30
 */
@Slf4j
public class ExcelUtil {

    /**
     * 创建excel文档
     *
     * @param getters list中map的key数组集合
     * @param headers excel的列名
     */
    public static void exportExcel(List list, String[] getters, String[] headers, Class clazz, HttpServletResponse response) {
        List<Method> methods = getMethodsByStrs(getters, clazz);
        // 创建.xls工作簿
        Workbook workbook = new XSSFWorkbook();
        // 创建第一个sheet（页），并命名
        Sheet sheet = workbook.createSheet("sheet1");
        // 手动设置列宽.第一个参数表示要为第几列设,第二个参数表示列的宽度,n为列高的像素数.
        for (int i = 0; i < getters.length; i++) {
            sheet.setColumnWidth((short) i, (short) (35.7 * 100));
        }
        // 创建第一行
        Row header = sheet.createRow(0);
        // 创建两种单元格格式
        CellStyle cellStyle1 = workbook.createCellStyle();
        CellStyle cellStyle2 = workbook.createCellStyle();
        // 标题字体
        Font font1 = workbook.createFont();
        // 正文字体
        Font font2 = workbook.createFont();
        // 标题加粗
        font1.setBoldweight(Font.BOLDWEIGHT_BOLD);
        // 设置两种单元格的样式
        setCellStype(cellStyle1, font1);
        setCellStype(cellStyle2, font2);
        //填充单元格
        cellStyle1.setFillPattern(CellStyle.SOLID_FOREGROUND);
        cellStyle2.setFillPattern(CellStyle.SOLID_FOREGROUND);
        cellStyle1.setFillForegroundColor(HSSFColor.AQUA.index);
        cellStyle2.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        //设置header
        for (int i = 0; i < headers.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(cellStyle1);
        }
        //设置data
        int headersNum = 1;
        for (int i = 0; i < list.size(); i++) {
            Row row = sheet.createRow(i + headersNum);
            for (int j = 0; j < methods.size(); j++) {
                try {
                    Object invoke = methods.get(j).invoke(list.get(i));
                    if (invoke != null) {
                        Cell cell = row.createCell(j);
                        cell.setCellStyle(cellStyle2);
                        cell.setCellValue(invoke.toString());
                    }
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        }
        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment;filename=test.xls");
        try {
            response.flushBuffer();
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private static void setCellStype(CellStyle cellStyle, Font font) {
        font.setFontHeightInPoints((short) 10);
        font.setColor(IndexedColors.BLACK.getIndex());
        cellStyle.setFont(font);
        cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        cellStyle.setBorderRight(CellStyle.BORDER_THIN);
        cellStyle.setBorderTop(CellStyle.BORDER_THIN);
        cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
    }

    private static List<Method> getMethodsByStrs(String[] getters, Class clazz) {
        List<Method> list = new ArrayList<>();
        for (String getter : getters) {
            try {
                list.add(clazz.getDeclaredMethod(getter));
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        return list;
    }
}
