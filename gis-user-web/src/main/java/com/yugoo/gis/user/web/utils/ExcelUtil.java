package com.yugoo.gis.user.web.utils;

import com.yugoo.gis.common.exception.GisRuntimeException;
import com.yugoo.gis.common.utils.SimpleDateUtil;
import com.yugoo.gis.pojo.po.ResourcePO;
import com.yugoo.gis.pojo.vo.ConsumerListVO;
import com.yugoo.gis.pojo.vo.ListVO;
import com.yugoo.gis.pojo.vo.ResourceVO;
import com.yugoo.gis.pojo.vo.StatisticCenterVO;
import com.yugoo.gis.pojo.vo.StatisticUserVO;
import lombok.Data;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Encoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author nihao
 * @create 2018/11/1
 **/
public class ExcelUtil {
    private static final Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

    private static final String EXCEL_XLS = "xls";
    private static final String EXCEL_XLSX = "xlsx";

    /**
     * 判断Excel的版本,获取Workbook
     * @param in
     * @param filename
     * @return
     * @throws IOException
     */
    public static Workbook getWorkbok(InputStream in, String filename) throws IOException {
        Workbook wb = null;
        if(filename.endsWith(EXCEL_XLS)){  //Excel 2003
            wb = new HSSFWorkbook(in);
        }
        else if(filename.endsWith(EXCEL_XLSX)){  // Excel 2007/2010
            wb = new XSSFWorkbook(in);
        }
        return wb;
    }

    public static  List<Map<String,String>> readResourceData(InputStream in, String filename) throws IOException {
        Workbook workbook = getWorkbok(in, filename);
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
            Object obj = getValue(cell);
            if (obj != null) {
                String headName = obj.toString().trim();
                headMap.put(headName, column++);
            }
        }
        List<Map<String,String>> list = new ArrayList<>();
        for (int i = 1; i <= rowCount; i ++) {
            Map<String,String> map = new HashMap<>();
            Row row = sheet.getRow(i);
            if (row != null) {
                for (String headName : headMap.keySet()) {
                    try {
                        Integer col = headMap.get(headName);
                        Cell cell = row.getCell(col);
                        if (cell != null) {
                            Object obj = getValue(cell);
                            if (obj != null) {
                                String value = obj.toString().trim();
                                map.put(headName, value);
                            }
                        }
                    } catch (Exception e) {
                        logger.error("读取数据错误, 第{}行,headName:{}", i, headName, e);
                        throw new GisRuntimeException("读取数据失败");
                    }
                }
            }
            if (!map.isEmpty()) {
                map.put("row", (i + 1) + "");
                list.add(map);
            }
        }
        return list;
    }

    public static Object getValue(Cell cell) {
        Object value = null;
        switch (cell.getCellTypeEnum()) {
            case _NONE:
                break;
            case NUMERIC:
                value = cell.getNumericCellValue();
                break;
            case STRING:
                value = cell.getStringCellValue();
                break;
            case FORMULA:
                break;
            case BLANK:
                break;
            case BOOLEAN:
                value = cell.getBooleanCellValue();
                break;
            case ERROR:
                value = cell.getErrorCellValue();
                break;
            default:
                break;
        }
        return value;
    }

    @Data
    static class NeedList {
        private Integer xmlWidth;
        private String title;
        private int sort;
        private String field;

        public NeedList(Integer xmlWidth, String title, int sort, String field) {
            this.xmlWidth = xmlWidth;
            this.title = title;
            this.sort = sort;
            this.field = field;
        }
    }

    public static final List<NeedList> consumerTitles = new ArrayList<>();
    public static final List<NeedList> resourceTitles = new ArrayList<>();
    public static final List<NeedList> statisticTitles = new ArrayList<>();
    public static final List<NeedList> statisticConsumerTitles = new ArrayList<>();
    public static final List<NeedList> userTitles = new ArrayList<>();
    public static final List<NeedList> customerInfoTitles = new ArrayList<>();

    static {
        /**
         * 客户信息导出
         */
        int consumerTitlesInt = 0;
        consumerTitles.add(new NeedList(100, "名称", consumerTitlesInt++, "@name"));
        consumerTitles.add(new NeedList(100, "建筑", consumerTitlesInt++, "buildingName"));
        consumerTitles.add(new NeedList(100, "楼层", consumerTitlesInt++, "@floor"));
        consumerTitles.add(new NeedList(100, "门牌号", consumerTitlesInt++, "@number"));
        consumerTitles.add(new NeedList(100, "地址", consumerTitlesInt++, "@position"));
        consumerTitles.add(new NeedList(100, "行业类别", consumerTitlesInt++, "@category"));
        consumerTitles.add(new NeedList(100, "公司性质", consumerTitlesInt++, "@nature"));
        consumerTitles.add(new NeedList(100, "公司人数", consumerTitlesInt++, "@peopleNum"));
        consumerTitles.add(new NeedList(100, "联系人", consumerTitlesInt++, "@linkman"));
        consumerTitles.add(new NeedList(100, "联系电话", consumerTitlesInt++, "@phone"));
        consumerTitles.add(new NeedList(100, "现有业务运营商", consumerTitlesInt++, "@operator"));
        consumerTitles.add(new NeedList(100, "现有业务资费", consumerTitlesInt++, "@expenses"));
        consumerTitles.add(new NeedList(100, "业务到期时间", consumerTitlesInt++, "expirationDateStr"));
        consumerTitles.add(new NeedList(100, "带宽", consumerTitlesInt++, "@bandwidth"));
        consumerTitles.add(new NeedList(100, "公司状态", consumerTitlesInt++, "@status"));
        consumerTitles.add(new NeedList(100, "法人", consumerTitlesInt++, "@legal"));
        consumerTitles.add(new NeedList(100, "集团代码", consumerTitlesInt++, "@groupCode"));
        consumerTitles.add(new NeedList(100, "集团等级", consumerTitlesInt++, "@groupGrade"));
        consumerTitles.add(new NeedList(100, "业务类型", consumerTitlesInt++, "serviceTypeName"));
        consumerTitles.add(new NeedList(100, "专线条数", consumerTitlesInt++, "@lineNum"));
        consumerTitles.add(new NeedList(100, "专线类型", consumerTitlesInt++, "@lineType"));
        consumerTitles.add(new NeedList(100, "专线开户时间", consumerTitlesInt++, "lineOpenDateStr"));
        consumerTitles.add(new NeedList(100, "专线状态", consumerTitlesInt++, "@lineStatus"));
        consumerTitles.add(new NeedList(100, "订购资费名称", consumerTitlesInt++, "@expensesName"));
        consumerTitles.add(new NeedList(100, "订购时间", consumerTitlesInt++, "orderTimeStr"));
        consumerTitles.add(new NeedList(100, "成员角色", consumerTitlesInt++, "@memberRole"));
        consumerTitles.add(new NeedList(100, "成员真实号码", consumerTitlesInt++, "@memberRoleRealNum"));
        consumerTitles.add(new NeedList(100, "成员侧资费名称", consumerTitlesInt++, "@memberExpensesName"));
        consumerTitles.add(new NeedList(100, "建档类型", consumerTitlesInt++, "typeName"));
        consumerTitles.add(new NeedList(100, "客户经理", consumerTitlesInt++, "userName"));
        consumerTitles.add(new NeedList(100, "客户经理工号", consumerTitlesInt++, "userNumber"));

        /**
         * 网络资源导出
         */
        int resourceTitlesInt = 0;
        resourceTitles.add(new NeedList(100, "外线ID", resourceTitlesInt++, "@primaryId"));
        resourceTitles.add(new NeedList(100, "地市名称", resourceTitlesInt++, "@cityName"));
        resourceTitles.add(new NeedList(100, "区县", resourceTitlesInt++, "@district"));
        resourceTitles.add(new NeedList(100, "街道", resourceTitlesInt++, "streetName"));
        resourceTitles.add(new NeedList(100, "乡镇", resourceTitlesInt++, "@villageName"));
        resourceTitles.add(new NeedList(100, "道路/行政村", resourceTitlesInt++, "@admStreetName"));
        resourceTitles.add(new NeedList(100, "片区/学校", resourceTitlesInt++, "@zoneName"));
        resourceTitles.add(new NeedList(100, "建筑", resourceTitlesInt++, "buildingName"));
        resourceTitles.add(new NeedList(100, "楼层", resourceTitlesInt++, "@floor"));
        resourceTitles.add(new NeedList(100, "户号", resourceTitlesInt++, "@number"));
        resourceTitles.add(new NeedList(100, "空闲端口数", resourceTitlesInt++, "@idelPortCount"));
        resourceTitles.add(new NeedList(100, "总端口数", resourceTitlesInt++, "@allPortCount"));
        resourceTitles.add(new NeedList(100, "用户场景一类", resourceTitlesInt++, "@sceneA"));
        resourceTitles.add(new NeedList(100, "用户场景二类", resourceTitlesInt++, "@sceneB"));
        resourceTitles.add(new NeedList(100, "覆盖场景", resourceTitlesInt++, "@overlayScene"));
        resourceTitles.add(new NeedList(100, "覆盖场景", resourceTitlesInt++, "@overlayScene"));
        resourceTitles.add(new NeedList(100, "中心位置经度", resourceTitlesInt++, "@longitude"));
        resourceTitles.add(new NeedList(100, "中心位置纬度", resourceTitlesInt++, "@latitude"));

        /**
         * 营销中心业务统计导出
         */
        int statisticTitlesInt = 0;
        statisticTitles.add(new NeedList(100, "区县", statisticTitlesInt++, "district"));
        statisticTitles.add(new NeedList(100, "营销中心", statisticTitlesInt++, "centerName"));
        statisticTitles.add(new NeedList(100, "未建档数量", statisticTitlesInt++, "@notArchiveCount"));
        statisticTitles.add(new NeedList(100, "基础建档数量", statisticTitlesInt++, "@basicArchiveCount"));
        statisticTitles.add(new NeedList(100, "有效建档数量", statisticTitlesInt++, "@effectiveArchiveCount"));
        statisticTitles.add(new NeedList(100, "总端口数", statisticTitlesInt++, "@wholePortCount"));
        statisticTitles.add(new NeedList(100, "已占用端口数", statisticTitlesInt++, "@usedPortCount"));
        statisticTitles.add(new NeedList(100, "专线渗透率", statisticTitlesInt++, "specialLineRatioStr"));
        statisticTitles.add(new NeedList(100, "酒店渗透率", statisticTitlesInt++, "hotelRatioStr"));
        statisticTitles.add(new NeedList(100, "商务动力渗透率", statisticTitlesInt++, "businessRatioStr"));

        /**
         * 即将到期业务统计导出
         */
        int statisticConsumerTitlesInt = 0;
        statisticConsumerTitles.add(new NeedList(100, "客户名称", statisticConsumerTitlesInt++, "@name"));
        statisticConsumerTitles.add(new NeedList(100, "业务到期时间", statisticConsumerTitlesInt++, "expirationDateStr"));
        statisticConsumerTitles.add(new NeedList(100, "现有业务运营商", statisticConsumerTitlesInt++, "@operator"));
        statisticConsumerTitles.add(new NeedList(100, "联系人", statisticConsumerTitlesInt++, "@linkman"));
        statisticConsumerTitles.add(new NeedList(100, "联系电话", statisticConsumerTitlesInt++, "@phone"));
        statisticConsumerTitles.add(new NeedList(150, "地址", statisticConsumerTitlesInt++, "@position"));

        /**
         * 客户经理业务统计导出
         */
        int userTitlesInt = 0;
        userTitles.add(new NeedList(100, "营销中心", userTitlesInt++, "centerName"));
        userTitles.add(new NeedList(100, "客户经理", userTitlesInt++, "userName"));
        userTitles.add(new NeedList(100, "基础建档数量", userTitlesInt++, "@basicArchiveCount"));
        userTitles.add(new NeedList(100, "已建档数量", userTitlesInt++, "@effectiveArchiveCount"));
        userTitles.add(new NeedList(100, "新建专线数量", userTitlesInt++, "@specialLineCount"));

        /**
         * 扫街扫铺统计导出
         */
        int customerInfoTitlesInt = 0;
        customerInfoTitles.add(new NeedList(100, "客户经理", customerInfoTitlesInt++, "userName"));
        customerInfoTitles.add(new NeedList(100, "要客组", customerInfoTitlesInt++, "groupName"));
        customerInfoTitles.add(new NeedList(100, "客户名称", customerInfoTitlesInt++, "@customerName"));
        customerInfoTitles.add(new NeedList(150, "地址", customerInfoTitlesInt++, "@address"));
        customerInfoTitles.add(new NeedList(100, "电话", customerInfoTitlesInt++, "@phone"));
        customerInfoTitles.add(new NeedList(80, "状态", customerInfoTitlesInt++, "statusStr"));
        customerInfoTitles.add(new NeedList(120, "办理业务", customerInfoTitlesInt++, "@transactedService"));
        customerInfoTitles.add(new NeedList(120, "办理业务子项", customerInfoTitlesInt++, "@transactedServiceSub"));
        customerInfoTitles.add(new NeedList(120, "预约业务", customerInfoTitlesInt++, "@bookedService"));
        customerInfoTitles.add(new NeedList(120, "预约业务子项", customerInfoTitlesInt++, "@bookedServiceSub"));
        customerInfoTitles.add(new NeedList(100, "随行人员", customerInfoTitlesInt++, "@partner"));
        customerInfoTitles.add(new NeedList(200, "备注", customerInfoTitlesInt++, "@remark"));
        customerInfoTitles.add(new NeedList(100, "创建时间", customerInfoTitlesInt++, "@ctime"));
    }

    private static String base64EncodeFileName(String fileName) {
        BASE64Encoder base64Encoder = new BASE64Encoder();
        try {
            return "=?UTF-8?B?"
                    + new String(base64Encoder.encode(fileName
                    .getBytes("UTF-8"))) + "?=";
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Data
    static class PreExport {
        private Workbook wb;
        private Sheet sheet;
        private CellStyle titleStyle;
        private CellStyle styleContent;
    }

    private static PreExport get(List<NeedList> needListList) {
        PreExport preExport = new PreExport();
        HSSFWorkbook wb = new HSSFWorkbook();
        preExport.setWb(wb);
        HSSFSheet sheet = wb.createSheet("sheet1");
        preExport.setSheet(sheet);
        HSSFCellStyle titleStyle = wb.createCellStyle();
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        titleStyle.setAlignment(HorizontalAlignment.CENTER);//水平居中
        Font fontA = wb.createFont();
        fontA.setFontHeight((short)(20*12));//字体大小
        fontA.setFontName("宋体");//字体
        fontA.setItalic(false);//是否倾斜
//        fontA.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); //粗体字
        titleStyle.setFont(fontA);
        titleStyle.setWrapText(true);//是否换行
        titleStyle.setBorderBottom(BorderStyle.THIN);
        titleStyle.setBorderLeft(BorderStyle.THIN);
        titleStyle.setBorderRight(BorderStyle.THIN);
        titleStyle.setBorderTop(BorderStyle.THIN);
        preExport.setTitleStyle(titleStyle);
        Font fontB = wb.createFont();
        fontB.setFontHeight((short)(20*12));//字体大小
        fontB.setFontName("宋体");//字体
        fontB.setItalic(false);//是否倾斜
//        fontB.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        HSSFCellStyle styleContent = wb.createCellStyle();
        styleContent.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        styleContent.setAlignment(HorizontalAlignment.CENTER);//水平居中
        styleContent.setFont(fontB);
        styleContent.setWrapText(true);//是否换行
        styleContent.setBorderBottom(BorderStyle.THIN);
        styleContent.setBorderLeft(BorderStyle.THIN);
        styleContent.setBorderRight(BorderStyle.THIN);
        styleContent.setBorderTop(BorderStyle.THIN);
        preExport.setStyleContent(styleContent);

        Row row0 = preExport.getSheet().createRow(0);
        row0.setHeight((short)500);
        for(int i = 0; i < needListList.size(); i++){
            Cell cell=row0.createCell(needListList.get(i).getSort());
            cell.setCellStyle(preExport.getTitleStyle());
            cell.setCellValue(needListList.get(i).getTitle());
            preExport.getSheet().setColumnWidth(i,needListList.get(i).getXmlWidth()*46);
        }
        return preExport;
    }

    public static Workbook prepareExport(List list, List<NeedList> needListList) throws NoSuchFieldException, IllegalAccessException {
        PreExport preExport = get(needListList);
        int r = 1;
        if (list != null && !list.isEmpty()) {
            for(int i=0;i<list.size();i++){
                Object object = list.get(i);
                Row row = preExport.getSheet().createRow(r++);
                for (NeedList needList : needListList) {
                    createCell(row, preExport.getStyleContent(), needList, object);
                }
            }
        }
        return preExport.getWb();
    }

    public static void export(HttpServletRequest request, HttpServletResponse response, Workbook workbook, String fileName) {
        ServletOutputStream outputStream=null;
        try{
            String filename = fileName + UUID.randomUUID().toString().substring(0,8)+".xls";
            // 先判断是什么浏览器
            String agent = request.getHeader("USER-AGENT");
            // 判断当前是什么浏览器
            if(agent.contains("Firefox")){
                // 采用BASE64编码
                filename = base64EncodeFileName(filename);
            }
            else{
                // IE GOOGLE
                filename = URLEncoder.encode(filename, "UTF-8");
                // URL编码会空格编成+
                filename = filename.replace("+", " ");
            }
            String mimeType = request.getSession().getServletContext().getMimeType(".xls");
            response.setContentType(mimeType);
            response.setHeader("Content-Disposition","attachment;filename="+filename);
            outputStream=response.getOutputStream();
            workbook.write(outputStream);
        }catch (Exception e){
            throw new RuntimeException(e);
        }finally {
            try{
                if(outputStream!=null){
                    outputStream.close();
                }
            }catch(Exception e){
                throw new RuntimeException(e);
            }
        }
    }

    private static Cell createCell(Row row, CellStyle cellStyle, NeedList needList, Object obj) throws NoSuchFieldException, IllegalAccessException {
        Cell cell = row.createCell(needList.getSort());
        cell.setCellStyle(cellStyle);
        Field field = null;
        if (needList.getField().startsWith("@")) {
            field = obj.getClass().getSuperclass().getDeclaredField(needList.getField().replace("@", ""));
        }
        else {
            field = obj.getClass().getDeclaredField(needList.getField());
        }
        field.setAccessible(true);
        Object value = field.get(obj);
        if (value != null) {
            if (value instanceof String) {
                cell.setCellValue((String) value);
            }
            else if (value instanceof Integer) {
                cell.setCellValue((Integer) value);
            }
            else if (value instanceof Double) {
                cell.setCellValue((Double) value);
            }
            else if (value instanceof Date) {
                cell.setCellValue(SimpleDateUtil.format((Date) value));
            }
            else {
                cell.setCellValue(String.valueOf(value));
            }
        }
        return cell;
    }
}
