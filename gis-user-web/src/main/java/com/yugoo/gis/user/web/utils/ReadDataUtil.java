package com.yugoo.gis.user.web.utils;

import com.yugoo.gis.common.exception.GisRuntimeException;
import com.yugoo.gis.pojo.excel.ExcelImport;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author nihao 2018/11/16
 */
public class ReadDataUtil<T> {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public List<T> readData(InputStream in, String filename, T t) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Workbook workbook = ExcelUtil.getWorkbok(in, filename);
        if (workbook == null) {
            throw new GisRuntimeException("excel文件格式错误");
        }
        int sheetCount = workbook.getNumberOfSheets();
        if (sheetCount == 0) {
            throw new GisRuntimeException("没有sheet页");
        }
        Sheet sheet = workbook.getSheetAt(0);
        int rowCount = sheet.getLastRowNum();
        if (rowCount == 0) {
            throw new GisRuntimeException("没有数据");
        }
        Map<String,Integer> headMap = new LinkedHashMap<>();
        Row headRow = sheet.getRow(0);
        int column = 0;
        for(Cell cell : headRow) {
            Object obj = ExcelUtil.getValue(cell);
            if (obj != null) {
                String headName = obj.toString().trim();
                headMap.put(headName, column++);
            }
        }

        Field[] fields = t.getClass().getDeclaredFields();
        Map<String,Field> fieldMap = new HashMap<>();
        for (Field field : fields) {
            try {
                ExcelImport excelImport = field.getAnnotation(ExcelImport.class);
                for (String name : excelImport.value()) {
                    field.setAccessible(true);
                    fieldMap.put(name, field);
                }
            } catch (NullPointerException e) {
            }
            if (field.getName().equals("row")) {
                field.setAccessible(true);
                fieldMap.put("excel_row_num", field);
            }
        }

        List<T> list = new ArrayList<>();
        for (int i = 1; i <= rowCount; i ++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                Class<T> clazz = (Class<T>) Class.forName(t.getClass().getName());
                T object = clazz.newInstance();
                fieldMap.get("excel_row_num").set(object, i + 1);
                for (String headName : headMap.keySet()) {
                    Field field = fieldMap.get(headName);
                    if (field != null) {
                        try {
                            Integer col = headMap.get(headName);
                            Cell cell = row.getCell(col);
                            if (cell != null) {
                                Object obj = ExcelUtil.getValue(cell);
                                if (obj != null) {
                                    Object realValue = null;
                                    if (field.getType().equals(String.class)) {
                                        if (obj instanceof Double) {
                                            double value = (Double) obj;
                                            if ((Math.round(value) - value) == 0) {
                                                realValue = String.valueOf((long) value);
                                            }
                                            else {
                                                realValue = String.valueOf(value);
                                            }
                                        }
                                        else {
                                            realValue = obj.toString().trim();
                                        }
                                    }
                                    else if (field.getType().equals(Integer.class)) {
                                        if (obj instanceof Double) {
                                            Double value = (Double) obj;
                                            realValue = value.intValue();
                                        }
                                        else {
                                            String value = obj.toString().trim();
                                            realValue = Integer.parseInt(value);
                                        }
                                    }
                                    else if (field.getType().equals(Double.class)) {
                                        if (obj instanceof Double) {
                                            realValue = obj;
                                        }
                                        else {
                                            String value = obj.toString().trim();
                                            realValue = Double.parseDouble(value);
                                        }
                                    }
                                    else if (field.getType().equals(Long.class)) {
                                        if (obj instanceof Double) {
                                            Double value = (Double) obj;
                                            realValue = value.longValue();
                                        }
                                        else {
                                            String value = obj.toString().trim();
                                            realValue = Long.parseLong(value);
                                        }
                                    }
                                    else if (field.getType().equals(BigDecimal.class)) {
                                        if (obj instanceof Double) {
                                            Double value = (Double) obj;
                                            realValue = new BigDecimal(value);
                                        }
                                        else {
                                            String value = obj.toString().trim();
                                            realValue = new BigDecimal(value);
                                        }
                                    }
                                    else {
                                        throw new Exception("未捕获的类型" + t.getClass().getName() + " --> " + field.getType().getName());
                                    }
                                    field.set(object, realValue);
                                }
                            }
                        } catch (Exception e) {
                            logger.error("读取数据错误, 第{}行,headName:{}", i + 1, headName, e);
                            throw new GisRuntimeException("读取数据错误,第" + (i + 1) + "行【" + headName + "】列");
                        }
                    }
                }
                list.add(object);
            }
        }
        return list;
    }

}
