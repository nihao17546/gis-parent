package com.yugoo.gis.user.web.utils;

import com.yugoo.gis.common.exception.GisRuntimeException;
import com.yugoo.gis.pojo.po.ResourcePO;
import com.yugoo.gis.pojo.vo.ConsumerListVO;
import com.yugoo.gis.pojo.vo.ListVO;
import com.yugoo.gis.pojo.vo.ResourceVO;
import com.yugoo.gis.pojo.vo.StatisticCenterVO;
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
    private static Workbook getWorkbok(InputStream in, String filename) throws IOException {
        Workbook wb = null;
        if(filename.endsWith(EXCEL_XLS)){  //Excel 2003
            wb = new HSSFWorkbook(in);
        }
        else if(filename.endsWith(EXCEL_XLSX)){  // Excel 2007/2010
            wb = new XSSFWorkbook(in);
        }
        return wb;
    }

    public static  List<ResourcePO> readResourceData(InputStream in, String filename) throws IOException {
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
        Row headRow = sheet.getRow(0);
        int column = 0;
        ResourceExcel resourceExcel = new ResourceExcel();
        for(Cell cell : headRow) {
            Object obj = getValue(cell);
            if (obj != null) {
                String headName = obj.toString().trim();
                if (headName.equals("地市名称")) {
                    resourceExcel.setCityName(column);
                }
                else if (headName.equals("区县")) {
                    resourceExcel.setDistrict(column);
                }
                else if (headName.equals("街道")) {
                    resourceExcel.setStreetName(column);
                }
                else if (headName.equals("乡镇")) {
                    resourceExcel.setVillageName(column);
                }
                else if (headName.equals("道路/行政村")) {
                    resourceExcel.setAdmStreetName(column);
                }
                else if (headName.equals("片区/学校")) {
                    resourceExcel.setZoneName(column);
                }
                else if (headName.equals("楼层")) {
                    resourceExcel.setFloor(column);
                }
                else if (headName.equals("户号")) {
                    resourceExcel.setNumber(column);
                }
                else if (headName.equals("空闲端口数")) {
                    resourceExcel.setIdelPortCount(column);
                }
                else if (headName.equals("总端口数")) {
                    resourceExcel.setAllPortCount(column);
                }
                else if (headName.equals("用户场景一类")) {
                    resourceExcel.setSceneA(column);
                }
                else if (headName.equals("用户场景二类")) {
                    resourceExcel.setSceneB(column);
                }
                else if (headName.equals("覆盖场景")) {
                    resourceExcel.setOverlayScene(column);
                }
                else if (headName.equals("小区/自然村/弄")) {
                    resourceExcel.setBuildingA(column);
                }
                else if (headName.equals("幢/号/楼")) {
                    resourceExcel.setBuildingB(column);
                }
                else if (headName.equals("单元号")) {
                    resourceExcel.setBuildingC(column);
                }
                else if (headName.equals("中心位置经度")) {
                    resourceExcel.setLo(column);
                }
                else if (headName.equals("中心位置纬度")) {
                    resourceExcel.setLa(column);
                }
            }
            column ++;
        }
// TODO: 2018/11/13  
        List<ResourcePO> resourcePOList = new ArrayList<>(rowCount - 1);
        for (int i = 1; i < rowCount; i ++) {
            try {
                Row row = sheet.getRow(i);
                ResourcePO resourcePO = new ResourcePO();
                if (resourceExcel.getCityName() != null) {
                    Cell cell = row.getCell(resourceExcel.getCityName());
                    if (cell != null) {
                        Object obj = getValue(cell);
                        if (obj != null) {
                            resourcePO.setCityName(obj.toString());
                        }
                    }
                }
                if (resourceExcel.getDistrict() != null) {
                    Cell cell = row.getCell(resourceExcel.getDistrict());
                    if (cell != null) {
                        Object obj = getValue(cell);
                        if (obj != null) {
                            resourcePO.setDistrict(obj.toString());
                        }
                    }
                }
                if (resourceExcel.getStreetName() != null) {
                    Cell cell = row.getCell(resourceExcel.getStreetName());
                    if (cell != null) {
                        Object obj = getValue(cell);
                        if (obj != null) {
                            resourcePO.setStreetName(obj.toString());
                        }
                    }
                }
                if (resourceExcel.getVillageName() != null) {
                    Cell cell = row.getCell(resourceExcel.getVillageName());
                    if (cell != null) {
                        Object obj = getValue(cell);
                        if (obj != null) {
                            resourcePO.setVillageName(obj.toString());
                        }
                    }
                }
                if (resourceExcel.getAdmStreetName() != null) {
                    Cell cell = row.getCell(resourceExcel.getAdmStreetName());
                    if (cell != null) {
                        Object obj = getValue(cell);
                        if (obj != null) {
                            resourcePO.setAdmStreetName(obj.toString());
                        }
                    }
                }
                if (resourceExcel.getZoneName() != null) {
                    Cell cell = row.getCell(resourceExcel.getZoneName());
                    if (cell != null) {
                        Object obj = getValue(cell);
                        if (obj != null) {
                            resourcePO.setZoneName(obj.toString());
                        }
                    }
                }
                if (resourceExcel.getFloor() != null) {
                    Cell cell = row.getCell(resourceExcel.getFloor());
                    if (cell != null) {
                        Object obj = getValue(cell);
                        if (obj != null) {
                            resourcePO.setFloor(obj.toString());
                        }
                    }
                }
                if (resourceExcel.getNumber() != null) {
                    Cell cell = row.getCell(resourceExcel.getNumber());
                    if (cell != null) {
                        Object obj = getValue(cell);
                        if (obj != null) {
                            resourcePO.setNumber(obj.toString());
                        }
                    }
                }
                if (resourceExcel.getAllPortCount() != null) {
                    Cell cell = row.getCell(resourceExcel.getAllPortCount());
                    if (cell != null) {
                        Object obj = getValue(cell);
                        if (obj != null) {
                            if (obj instanceof Double) {
                                resourcePO.setAllPortCount(((Double)obj).intValue());
                            }
                            else if (obj instanceof Integer) {
                                resourcePO.setAllPortCount((Integer)obj);
                            }
                            else if (obj instanceof Float) {
                                resourcePO.setAllPortCount(((Float)obj).intValue());
                            }
                            else {
                                resourcePO.setAllPortCount(Integer.parseInt(obj.toString()));
                            }
                        }
                    }
                }
                if (resourceExcel.getIdelPortCount() != null) {
                    Cell cell = row.getCell(resourceExcel.getIdelPortCount());
                    if (cell != null) {
                        Object obj = getValue(cell);
                        if (obj != null) {
                            if (obj instanceof Double) {
                                resourcePO.setIdelPortCount(((Double)obj).intValue());
                            }
                            else if (obj instanceof Integer) {
                                resourcePO.setIdelPortCount((Integer)obj);
                            }
                            else if (obj instanceof Float) {
                                resourcePO.setIdelPortCount(((Float)obj).intValue());
                            }
                            else {
                                resourcePO.setIdelPortCount(Integer.parseInt(obj.toString()));
                            }
                        }
                    }
                }
                if (resourceExcel.getSceneA() != null) {
                    Cell cell = row.getCell(resourceExcel.getSceneA());
                    if (cell != null) {
                        Object obj = getValue(cell);
                        if (obj != null) {
                            resourcePO.setSceneA(obj.toString());
                        }
                    }
                }
                if (resourceExcel.getSceneB() != null) {
                    Cell cell = row.getCell(resourceExcel.getSceneB());
                    if (cell != null) {
                        Object obj = getValue(cell);
                        if (obj != null) {
                            resourcePO.setSceneB(obj.toString());
                        }
                    }
                }
                if (resourceExcel.getOverlayScene() != null) {
                    Cell cell = row.getCell(resourceExcel.getOverlayScene());
                    if (cell != null) {
                        Object obj = getValue(cell);
                        if (obj != null) {
                            resourcePO.setOverlayScene(obj.toString());
                        }
                    }
                }
                if (checkResource(resourcePO)) {
                    resourcePO.setBuildingId(0);
                    resourcePO.setLatitude(-999.0);
                    resourcePO.setLongitude(-999.0);
                    if (resourcePO.getAllPortCount() == null) {
                        resourcePO.setAllPortCount(0);
                    }
                    if (resourcePO.getIdelPortCount() == null) {
                        resourcePO.setIdelPortCount(0);
                    }
                    resourcePOList.add(resourcePO);
                }
            } catch (Exception e) {
                logger.error("解析excel第{}行错误", (i + 1), e);
                throw new GisRuntimeException("解析excel第" + (i + 1) + "行错误,错误信息:" + e.getMessage());
            }
        }
        return resourcePOList;
    }

    private static boolean checkResource(ResourcePO resourcePO) {
        if (resourcePO.getId() != null) {
            return true;
        }
        if (resourcePO.getBuildingId() != null) {
            return true;
        }
        if (resourcePO.getDistrict() != null) {
            return true;
        }
        if (resourcePO.getFloor() != null) {
            return true;
        }
        if (resourcePO.getNumber() != null) {
            return true;
        }
        if (resourcePO.getAllPortCount() != null) {
            return true;
        }
        if (resourcePO.getIdelPortCount() != null) {
            return true;
        }
        if (resourcePO.getSceneA() != null) {
            return true;
        }
        if (resourcePO.getSceneB() != null) {
            return true;
        }
        if (resourcePO.getOverlayScene() != null) {
            return true;
        }
        if (resourcePO.getLatitude() != null) {
            return true;
        }
        if (resourcePO.getLongitude() != null) {
            return true;
        }
        if (resourcePO.getCtime() != null) {
            return true;
        }
        return false;
    }

    private static Object getValue(Cell cell) {
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
    static class ResourceExcel {
        private Integer district;
        private Integer cityName;
        private Integer streetName;
        private Integer villageName;
        private Integer admStreetName;
        private Integer zoneName;
        private Integer floor;
        private Integer number;
        private Integer allPortCount;
        private Integer idelPortCount;
        private Integer sceneA;
        private Integer sceneB;
        private Integer overlayScene;
        private Integer buildingA;
        private Integer buildingB;
        private Integer buildingC;
        private Integer lo;
        private Integer la;
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

    private static List<NeedList> resourceTitles = new ArrayList<>();
    private static List<NeedList> statisticTitles = new ArrayList<>();
    private static List<NeedList> statisticConsumerTitles = new ArrayList<>();
    private static List<NeedList> consumerTitles = new ArrayList<>();

    static {
        int resourceTitlesInt = 0;
        resourceTitles.add(new NeedList(100, "地市名称", resourceTitlesInt++, "@cityName"));
        resourceTitles.add(new NeedList(100, "区县", resourceTitlesInt++, "@district"));
        resourceTitles.add(new NeedList(100, "街道", resourceTitlesInt++, "@streetName"));
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

        statisticTitles.add(new NeedList(100, "区县", 0, "district"));
        statisticTitles.add(new NeedList(100, "营销中心", 1, "centerName"));
        statisticTitles.add(new NeedList(100, "未建档数量", 2, "@notArchiveCount"));
        statisticTitles.add(new NeedList(100, "基础建档数量", 3, "@basicArchiveCount"));
        statisticTitles.add(new NeedList(100, "有效建档数量", 4, "@effectiveArchiveCount"));
        statisticTitles.add(new NeedList(100, "总端口数", 5, "@wholePortCount"));
        statisticTitles.add(new NeedList(100, "已占用端口数", 6, "@usedPortCount"));
        statisticTitles.add(new NeedList(100, "专线渗透率", 7, "specialLineRatioStr"));
        statisticTitles.add(new NeedList(100, "酒店渗透率", 8, "hotelRatioStr"));
        statisticTitles.add(new NeedList(100, "商务动力渗透率", 9, "businessRatioStr"));

        statisticConsumerTitles.add(new NeedList(100, "客户名称", 0, "@name"));
        statisticConsumerTitles.add(new NeedList(100, "业务到期时间", 1, "expirationDateStr"));
        statisticConsumerTitles.add(new NeedList(100, "现有业务运营商", 2, "@operator"));
        statisticConsumerTitles.add(new NeedList(100, "联系人", 3, "@linkman"));
        statisticConsumerTitles.add(new NeedList(100, "联系电话", 4, "@phone"));
        statisticConsumerTitles.add(new NeedList(150, "地址", 5, "@position"));

//        consumerTitles.add(new NeedList(100, "名称", 0));
//        consumerTitles.add(new NeedList(100, "建筑", 1));
//        consumerTitles.add(new NeedList(100, "楼层", 2));
//        consumerTitles.add(new NeedList(100, "门牌号", 3));
//        consumerTitles.add(new NeedList(100, "行业类别", 4));
//        consumerTitles.add(new NeedList(100, "公司性质", 5));
//        consumerTitles.add(new NeedList(100, "地址", 6));
//        consumerTitles.add(new NeedList(100, "公司人数", 7));
//        consumerTitles.add(new NeedList(100, "联系人", 8));
//        consumerTitles.add(new NeedList(100, "联系电话", 9));
//        consumerTitles.add(new NeedList(100, "现有业务运营商", 10));
//        consumerTitles.add(new NeedList(100, "现有业务资费", 11));
//        consumerTitles.add(new NeedList(100, "业务到期时间", 12));
//        consumerTitles.add(new NeedList(100, "带宽", 13));
//        consumerTitles.add(new NeedList(100, "业务类型", 14));
//        consumerTitles.add(new NeedList(100, "公司状态", 15));
//        consumerTitles.add(new NeedList(100, "法人", 16));
//        consumerTitles.add(new NeedList(100, "专线条数", 17));
//        consumerTitles.add(new NeedList(100, "专线类型", 18));
//        consumerTitles.add(new NeedList(100, "专线开户时间", 19));
//        consumerTitles.add(new NeedList(100, "专线状态", 20));
//        consumerTitles.add(new NeedList(100, "订购资费名称", 21));
//        consumerTitles.add(new NeedList(100, "订购时间", 22));
//        consumerTitles.add(new NeedList(100, "成员角色", 23));
//        consumerTitles.add(new NeedList(100, "成员真实号码", 24));
//        consumerTitles.add(new NeedList(100, "成员侧资费名称", 25));
//        consumerTitles.add(new NeedList(100, "集团代码", 26));
//        consumerTitles.add(new NeedList(100, "集团等级", 27));
//        consumerTitles.add(new NeedList(100, "建档类型", 28));
//        consumerTitles.add(new NeedList(100, "客户经理", 29));
//        consumerTitles.add(new NeedList(100, "客户经理工号", 30));
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

    public static Workbook exportStatisticCenter(List<StatisticCenterVO> list) throws NoSuchFieldException, IllegalAccessException {
        PreExport preExport = get(statisticTitles);
        int r = 1;
        if (list != null && !list.isEmpty()) {
            for(int i=0;i<list.size();i++){
                StatisticCenterVO statisticCenterVO = list.get(i);
                Row row = preExport.getSheet().createRow(r++);
                for (NeedList needList : statisticTitles) {
                    createCell(row, preExport.getStyleContent(), needList, statisticCenterVO);
                }
            }
        }
        return preExport.getWb();
    }

    public static Workbook exportResource(ListVO<ResourceVO> resourceVOListVO) throws NoSuchFieldException, IllegalAccessException {
        PreExport preExport = get(resourceTitles);
        int r = 1;
        if (resourceVOListVO != null) {
            for(int i=0;i<resourceVOListVO.getTotalCount();i++){
                ResourceVO resourceVO = resourceVOListVO.getList().get(i);
                Row row = preExport.getSheet().createRow(r++);
                for (NeedList needList : resourceTitles) {
                    createCell(row, preExport.getStyleContent(), needList, resourceVO);
                }
            }
        }

        return preExport.getWb();
    }

    public static Workbook exportStatisticConsumer(ListVO<ConsumerListVO> listVO) throws NoSuchFieldException, IllegalAccessException {
        PreExport preExport = get(statisticConsumerTitles);
        int r = 1;
        if (listVO != null) {
            for(int i=0;i<listVO.getTotalCount();i++){
                ConsumerListVO consumerListVO = listVO.getList().get(i);
                Row row = preExport.getSheet().createRow(r++);
                for (NeedList needList : statisticConsumerTitles) {
                    createCell(row, preExport.getStyleContent(), needList, consumerListVO);
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
                cell.setCellValue((Date) value);
            }
            else {
                cell.setCellValue(String.valueOf(value));
            }
        }
        return cell;
    }
}
