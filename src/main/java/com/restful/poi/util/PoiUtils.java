package com.restful.poi.util;

import com.restful.common.thread.manage.AsyncManager;
import com.restful.poi.model.Excel;
import com.restful.poi.model.ExcelHead;
import com.restful.poinew.ExcelReaderUtil;
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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

    public static final String[] ENTITY_NAME = {"orderId", "onlineOrderId", "shopId", "shopName", "buyerName",
            "orderTime", "buyTime", "sendTime", "shouldPay", "hasPay", "discountPay", "fare", "status", "shopStatus",
            "errorType", "courierCompany", "trackingNumber", "consigneeName", "province", "city", "district", "street",
            "address", "mobilePhone", "fixedTelephone", "orderType", "buyerMessage", "orderRemark", "sendStorehouse",
            "tag", "payNumber", "platform", "childOrderNumber", "originalOnlineOrder", "styleNumber", "produceNumber",
            "produceName", "colorAndFormat", "amount", "producePrice", "produceMoney", "gift", "childOrderStatus",
            "costPrice", "resendAmount", "resendAmountReal", "resendMoney"};

    public static final String[] EXCEL_NAME = {"订单号", "线上订单号", "店铺编号", "店铺",
            "买家账号", "下单时间", "付款日期", "发货日期", "应付金额", "已付金额",
            "抵扣金额", "运费", "状态", "店铺状态", "异常类型", "快递公司",
            "快递单号", "收货人姓名", "省份", "城市", "区县", "街道", "地址",
            "手机", "固话", "订单类型", "买家留言", "订单备注", "发货仓",
            "标签", "支付单号", "平台站点", "子订单编号", "原始线上订单号",
            "款号", "商品编码", "商品名称", "颜色及规格", "数量", "商品单价",
            "商品金额", "是否赠品", "子订单状态", "成本价", "申请退货数量", "实退数量", "订单退款金额"};

    public static final int[] SIZE = {30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30,
            30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30};

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
     * @param clazz    类
     * @param workbook   工作簿对象
     * @param excelHeads excel与entity对应关系实体
     * @param <T>
     * @return
     * @throws Exception
     */
    private static <T> List<T> readExcel(Class<T> clazz, Workbook workbook, List<ExcelHead> excelHeads) throws Exception {
        List<T> beans = new ArrayList<>(500 + 4);
        //循环中需要使用的变量 提前声明
        Row dataRow;
        T instance;
        for (int sheetIndex = 0, sheetNum = workbook.getNumberOfSheets(); sheetIndex < sheetNum; sheetIndex++) {
            //开始解析工作表
            Sheet sheet = workbook.getSheetAt(sheetIndex);
            if (sheet == null) {
                continue;
            }
            int firstRowNum = sheet.getFirstRowNum();
            Row head = sheet.getRow(firstRowNum);
            if (head == null) {
                continue;
            }
            int lastRowNum = sheet.getLastRowNum();
            short firstCellNum = head.getFirstCellNum();
            short lastCellNum = head.getLastCellNum();
            Field[] fields = clazz.getDeclaredFields();

            for (int rowIndex = firstRowNum + 1; rowIndex <= lastRowNum; rowIndex++) {
                dataRow = sheet.getRow(rowIndex);
                if (dataRow == null || checkRowNull(dataRow)) {
                    continue;
                }
                //非头部映射方式，默认不校验是否为空，提高效率
                if (CollectionUtils.isEmpty(excelHeads)) {
                    firstCellNum = dataRow.getFirstCellNum();
                    lastCellNum = dataRow.getLastCellNum();
                }
                instance = clazz.newInstance();
                matchObjectField(clazz, excelHeads, dataRow, instance, head, firstCellNum, lastCellNum, fields);
                beans.add(instance);
                //如果集合中有数据并且数据大于 500 条，就异步入库并清空该集合
                if (beans.size() > 500) {
                    AsyncManager.getInstance().setFixedThreadPool(AsyncFactory.saveData((List<Excel>) beans));
                    beans.clear();
                }
            }
            //不满足数据大于 500 条，但是也是有数据的。异步执行入库并清空该集合
            if (beans.size() > 0) {
                AsyncManager.getInstance().setFixedThreadPool(AsyncFactory.saveData((List<Excel>) beans));
                beans.clear();
            }
        }
        return beans;
    }

    /**
     * 匹配对象字段
     */
    private static <T> void matchObjectField(Class<T> clazz, List<ExcelHead> excelHeads, Row dataRow, T instance, Row head,
                                             short firstCellNum, short lastCellNum, Field[] fields)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Cell headCell;
        String headName;
        Cell cell;
        ExcelHead eHead;
        for (int cellIndex = firstCellNum; cellIndex < lastCellNum; cellIndex++) {
            headCell = head.getCell(cellIndex);
            if (headCell == null) {
                continue;
            }
            headCell.setCellType(Cell.CELL_TYPE_STRING);
            headName = headCell.getStringCellValue().trim();
            if (StringUtils.isEmpty(headName)) {
                continue;
            }
            cell = dataRow.getCell(cellIndex);
            if (!CollectionUtils.isEmpty(excelHeads)) {
                eHead = excelHeads.get(cellIndex);
                headName = eHead.getEntityName();
            }
            reflectionObjectMethod(clazz, instance, cell, headName, fields[cellIndex]);
        }
    }

    /**
     *  反射调用对象方法，给对象赋值
     */
    private static <T> void reflectionObjectMethod(Class<T> tClass, T instance, Cell cell, String headName, Field field)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        //不匹配则直接 return
        if (!headName.equalsIgnoreCase(field.getName())){
            return;
        }
        String methodName = MethodUtils.setMethodName(field.getName());
        Method method = tClass.getMethod(methodName, field.getType());
        if (isDateField(field)) {
            Date date = null;
            if (cell != null && org.apache.commons.lang3.StringUtils.isNotBlank(cell.getStringCellValue())) {
                date = new Date(cell.getStringCellValue());
            }
            method.invoke(instance, date);
        } else {
            String value = "";
            if (cell != null) {
                cell.setCellType(Cell.CELL_TYPE_STRING);
                value = cell.getStringCellValue();
            }
            method.invoke(instance, ExcelReaderUtil.convertType(field.getType().getName(), value.trim()));
        }
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
            String[] entityName = {"orderId", "onlineOrderId", "shopId", "shopName", "buyerName", "orderTime", "buyTime", "sendTime",
            "shouldPay", "hasPay", "discountPay", "fare", "status", "shopStatus", "errorType", "courierCompany", "trackingNumber",
             "consigneeName", "province", "city", "district", "street", "address", "mobilePhone", "fixedTelephone", "orderType",
             "buyerMessage", "orderRemark", "sendStorehouse", "tag", "payNumber", "platform", "childOrderNumber", "originalOnlineOrder",
             "styleNumber", "produceNumber", "produceName", "colorAndFormat", "amount", "producePrice", "produceMoney", "gift",
             "childOrderStatus", "costPrice", "resendAmount", "resendAmountReal", "resendMoney"};
            String[] excelName = {"订单号", "线上订单号", "店铺编号", "店铺", "买家账号", "下单时间", "付款日期", "发货日期", "应付金额",
            "已付金额", "抵扣金额", "运费", "状态", "店铺状态", "异常类型", "快递公司", "快递单号", "收货人姓名", "省份", "城市", "区县",
            "街道", "地址", "手机", "固话", "订单类型", "买家留言", "订单备注", "发货仓", "标签", "支付单号", "平台站点", "子订单编号",
            "原始线上订单号", "款号", "商品编码", "商品名称", "颜色及规格", "数量", "商品单价", "商品金额", "是否赠品", "子订单状态",
             "成本价", "申请退货数量", "实退数量", "订单退款金额"};
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
