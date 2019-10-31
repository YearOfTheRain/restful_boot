package com.restful.poi.util;

import com.restful.common.thread.manage.AsyncManager;
import com.restful.poi.constant.ExcelConstansts;
import com.restful.poi.model.Excel;
import com.restful.poi.model.ExcelHead;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @author LiShuLin
 * @version v1.0
 * @program restful_boot
 * @description excle 数据解析
 * @date 2019-09-28 13:58
 */
public class PoiUtils {


    private static final String SHORT_DATA_FORMAT = "yyyy/MM/dd";

    public static final String[] ENTITY_NAME = {"orderId", "onlineOrderId", "shopId", "shopName", "buyerName", "orderTime", "buyTime", "sendTime", "shouldPay", "hasPay", "discountPay", "fare", "status", "shopStatus", "errorType", "courierCompany", "trackingNumber", "consigneeName", "province", "city", "district", "street", "address", "mobilePhone", "fixedTelephone", "orderType", "buyerMessage", "orderRemark", "sendStorehouse", "tag", "payNumber", "platform", "childOrderNumber", "originalOnlineOrder", "styleNumber", "produceNumber", "produceName", "colorAndFormat", "amount", "producePrice", "produceMoney", "gift", "childOrderStatus", "costPrice", "resendAmount", "resendAmountReal", "resendMoney"};

    public static final String[] EXCEL_NAME = {"订单号", "线上订单号", "店铺编号", "店铺", "买家账号", "下单时间", "付款日期", "发货日期", "应付金额", "已付金额", "抵扣金额", "运费", "状态", "店铺状态", "异常类型", "快递公司", "快递单号", "收货人姓名", "省份", "城市", "区县", "街道", "地址", "手机", "固话", "订单类型", "买家留言", "订单备注", "发货仓", "标签", "支付单号", "平台站点", "子订单编号", "原始线上订单号", "款号", "商品编码", "商品名称", "颜色及规格", "数量", "商品单价", "商品金额", "是否赠品", "子订单状态", "成本价", "申请退货数量", "实退数量", "订单退款金额"};

    public static final int[] SIZE = {30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30};

    /*** 默认数字类的值*/
    public static final String DEFAULT_NUM_VALUE = "0";

    /**
     * Excel表头对应Entity属性 解析封装javabean
     *
     * @param classzz    类
     * @param in         excel流
     * @param fileName   文件名
     * @param excelHeads excel表头与entity属性对应关系
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> List<T> readExcelToEntity(Class<T> classzz, InputStream in, String fileName, List<ExcelHead> excelHeads) throws Exception {
        //是否EXCEL文件
        checkFile(fileName);
        //兼容新老版本
        Workbook workbook = getWorkBoot(in, fileName);
        //解析Excel
        List<T> excelForBeans = readExcel(classzz, workbook, excelHeads);
        return excelForBeans;
    }

    public static <T> List<T> readExcelToEntity(Class<T> classzz, File file, String fileName, List<ExcelHead> excelHeads) throws Exception {
        //是否EXCEL文件
        checkFile(fileName);
        //兼容新老版本
        Workbook workbook = getWorkBoot(file, fileName);
        //解析Excel
        List<T> excelForBeans = readExcel(classzz, workbook, excelHeads);
        return excelForBeans;
    }

    /**
     * 解析Excel转换为Entity
     *
     * @param classzz  类
     * @param in       excel流
     * @param fileName 文件名
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> List<T> readExcelToEntity(Class<T> classzz, InputStream in, String fileName) throws Exception {
        return readExcelToEntity(classzz, in, fileName, null);
    }

    /**
     * 校验是否是Excel文件
     *
     * @param fileName
     * @throws Exception
     */
    public static void checkFile(String fileName) throws Exception {
        if (notExcel(fileName)) {
            throw new Exception("不是Excel文件！");
        }
    }

    private static boolean notExcel(String fileName) {
        return !isExcel(fileName);
    }

    private static boolean isExcel(String fileName) {
        return !StringUtils.isEmpty(fileName) && (fileName.endsWith(".xlsx") || fileName.endsWith(".xls"));
    }

    /**
     * 兼容新老版Excel
     *
     * @param in
     * @param fileName
     * @return
     * @throws IOException
     */
    private static Workbook getWorkBoot(InputStream in, String fileName) throws IOException {
        if (fileName.endsWith(".xlsx")) {
            return new XSSFWorkbook(in);
        } else {
            return new HSSFWorkbook(in);
        }
    }

    private static Workbook getWorkBoot(File file, String fileName) throws IOException, InvalidFormatException {
        if (fileName.endsWith(".xlsx")) {
            return new XSSFWorkbook(file);
        } else {
            return new HSSFWorkbook(new FileInputStream(file));
        }
    }

    private static boolean checkRowNull(Row row) {
        Iterator<Cell> iterator = row.iterator();
        Cell cell = iterator.next();
        return cell.getCellType() == Cell.CELL_TYPE_BLANK;
    }

    /**
     * 解析Excel
     *
     * @param classzz    类
     * @param workbook   工作簿对象
     * @param excelHeads excel与entity对应关系实体
     * @param <T>
     * @return
     * @throws Exception
     */
    private static <T> List<T> readExcel(Class<T> classzz, Workbook workbook, List<ExcelHead> excelHeads) throws Exception {
        List<T> beans = new ArrayList<>();
        //读取工作表数量
        int sheetNum = workbook.getNumberOfSheets();
        int flag = 0;
        //循环中需要使用的变量 提前声明
        Row dataRow;
        Cell headCell;
        T instance;
        Cell cell;
        String headName;
        ExcelHead eHead = null;
        String methodName;
        Method method;
        Field field;

        for (int sheetIndex = 0; sheetIndex < sheetNum; sheetIndex++) {
            //开始解析工作表
            Sheet sheet = workbook.getSheetAt(sheetIndex);
            if (sheet == null) {
                continue;
            }
            int firstRowNum = sheet.getFirstRowNum();
            int lastRowNum = sheet.getLastRowNum();
            Row head = sheet.getRow(firstRowNum);
            if (head == null) {
                continue;
            }
            short firstCellNum = head.getFirstCellNum();
            short lastCellNum = head.getLastCellNum();
            Field[] fields = classzz.getDeclaredFields();

            for (int rowIndex = firstRowNum + 1; rowIndex <= lastRowNum; rowIndex++) {
                dataRow = sheet.getRow(rowIndex);
                flag++;
                if (dataRow == null || checkRowNull(dataRow)) {
                    continue;
                }
                instance = classzz.newInstance();
                //非头部映射方式，默认不校验是否为空，提高效率
                if (CollectionUtils.isEmpty(excelHeads)) {
                    firstCellNum = dataRow.getFirstCellNum();
                    lastCellNum = dataRow.getLastCellNum();
                }
                for (int cellIndex = firstCellNum; cellIndex < lastCellNum; cellIndex++) {
                    headCell = head.getCell(cellIndex);
                    if (headCell == null) {
                        continue;
                    }
                    cell = dataRow.getCell(cellIndex);
                    headCell.setCellType(Cell.CELL_TYPE_STRING);
                    headName = headCell.getStringCellValue().trim();
                    if (StringUtils.isEmpty(headName)) {
                        continue;
                    }
                    if (!CollectionUtils.isEmpty(excelHeads)) {
                        eHead = excelHeads.get(cellIndex);
                        headName = eHead.getEntityName();
                    }
                    field = fields[cellIndex];
                    if (headName.equalsIgnoreCase(field.getName())) {
                        methodName = MethodUtils.setMethodName(field.getName());
                        method = classzz.getMethod(methodName, field.getType());
                        if (isDateField(field)) {
                            Date date = null;
                            if (cell != null && org.apache.commons.lang3.StringUtils.isNotBlank(cell.getStringCellValue())) {
                                date = new Date(cell.getStringCellValue());
                            }
                            method.invoke(instance, date);
                        } else {
                            String value = null;
                            if (cell != null) {
                                cell.setCellType(Cell.CELL_TYPE_STRING);
                                value = cell.getStringCellValue();
                            }
                            if (StringUtils.isEmpty(value)) {
                                value = "";
                            }
                            method.invoke(instance, convertType(field.getType().getName(), value.trim()));
                        }
                    }
                }
                beans.add(instance);
                //当500次循环后 如果集合中有数据 就异步入库 并清空该集合
                if (flag == 500 && beans.size() > 0) {
                    AsyncManager.getInstance().setFixedThreadPool(AsyncFactory.saveData((List<Excel>) beans));
                    flag = 0;
                    beans.clear();
                }
                //当数据不足分批插入条时 进入该方法 如果集合中有数据 就异步入库 并清空该集合
                if (rowIndex == lastRowNum && beans.size() > 0) {
                    AsyncManager.getInstance().setFixedThreadPool(AsyncFactory.saveData((List<Excel>) beans));
                    flag = 0;
                    beans.clear();
                }
            }
        }
        return beans;
    }

    /**
     * 是否日期字段
     *
     * @param field
     * @return
     */
    private static boolean isDateField(Field field) {
        return (Date.class == field.getType());
    }

    /**
     * 空值校验
     *
     * @param excelHead
     * @throws Exception
     */
    private static void volidateValueRequired(ExcelHead excelHead, String sheetName, int rowIndex) throws Exception {
        if (excelHead != null && excelHead.isRequired()) {
            throw new Exception("《" + sheetName + "》第" + (rowIndex + 1) + "行:\"" + excelHead.getExcelName() + "\"不能为空！");
        }
    }

    /**
     * 类型转换
     *
     * @param classType
     * @param value
     * @return
     */
    private static Object convertType(String classType, String value) {
        //防止字符串为 null 时，转换数值类型发生空指针异常
        if (org.apache.commons.lang3.StringUtils.isBlank(value)) {
            value = DEFAULT_NUM_VALUE;
        }
        if (ExcelConstansts.INTEGER_TYPE.equals(classType)) {
            return Integer.valueOf(value);
        }
        if (ExcelConstansts.SHORT_TYPE.equals(classType)) {
            return Short.valueOf(value);
        }
        if (ExcelConstansts.BYTE_TYPE.equals(classType)) {
            return Byte.valueOf(value);
        }
        if (ExcelConstansts.CHAR_TYPE.equals(classType)) {
            return value.charAt(0);
        }
        if (ExcelConstansts.LONG_TYPE.equals(classType)) {
            return Long.valueOf(value);
        }
        if (ExcelConstansts.FLOAT_TYPE.equals(classType)) {
            return Float.valueOf(value);
        }
        if (ExcelConstansts.DOUBLE_TYPE.equals(classType)) {
            return Double.valueOf(value);
        }
        if (ExcelConstansts.BOOLEAN_TYPE.equals(classType)) {
            return Boolean.valueOf(value.toLowerCase());
        }
        if (ExcelConstansts.BIG_DECIMAL_TYPE.equals(classType)) {
            return new BigDecimal(value);
        }
        //对 String 类进行还原 null 的操作
        if (DEFAULT_NUM_VALUE.equals(value)) {
            return null;
        }
        return value;
    }

    private static void accept(Excel excel) {
        System.out.println(excel.toString());
    }

    /**
     * 方法描述: 获取运行时长 结果
     *
     * @param startTime 开始时间
     * @return java.lang.Long
     * @author LiShuLin
     * @date 2019/10/8
     */
    public static Long getRunningTime(long startTime) {
        return (System.currentTimeMillis() - startTime);
    }

    /*public static void main(String[] args) {
        File file = new File("D:\\小程序ERP导入字段.xlsx");
        FileInputStream in = null;
        long start = System.currentTimeMillis();
        System.out.println("------------------开始---------------------");
        long size = 0;
        try {
            in = new FileInputStream(file);
            List<ExcelHead> excelHeads = new ArrayList<>(50);
            String[] entityName = {"orderId", "onlineOrderId", "shopId", "shopName", "buyerName", "orderTime", "buyTime", "sendTime", "shouldPay", "hasPay", "discountPay", "fare", "status", "shopStatus", "errorType", "courierCompany", "trackingNumber", "consigneeName", "province", "city", "district", "street", "address", "mobilePhone", "fixedTelephone", "orderType", "buyerMessage", "orderRemark", "sendStorehouse", "tag", "payNumber", "platform", "childOrderNumber", "originalOnlineOrder", "styleNumber", "produceNumber", "produceName", "colorAndFormat", "amount", "producePrice", "produceMoney", "gift", "childOrderStatus", "costPrice", "resendAmount", "resendAmountReal", "resendMoney"};
            String[] excelName = {"订单号", "线上订单号", "店铺编号", "店铺", "买家账号", "下单时间", "付款日期", "发货日期", "应付金额", "已付金额", "抵扣金额", "运费", "状态", "店铺状态", "异常类型", "快递公司", "快递单号", "收货人姓名", "省份", "城市", "区县", "街道", "地址", "手机", "固话", "订单类型", "买家留言", "订单备注", "发货仓", "标签", "支付单号", "平台站点", "子订单编号", "原始线上订单号", "款号", "商品编码", "商品名称", "颜色及规格", "数量", "商品单价", "商品金额", "是否赠品", "子订单状态", "成本价", "申请退货数量", "实退数量", "订单退款金额"};
            for (int i = 0; i < excelName.length; i++) {
                excelHeads.add(new ExcelHead(excelName[i], entityName[i]));
            }
            TimerTask timerTask = AsyncFactory.resolveExcel(in, file.getName(), excelHeads);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("------------------结束---------------------");
        System.out.println("共扫描取得" + size + "条数据，花费 " + (System.currentTimeMillis() - start) + "毫秒");
    }*/
}
