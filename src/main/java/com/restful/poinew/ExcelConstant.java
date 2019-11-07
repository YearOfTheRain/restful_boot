package com.restful.poinew;

/**
 * @author Shulin Li
 * @version v1.0
 * @program restful_boot
 * @description Excel 使用的常量
 * @date 2019-11-06 15:59
 */
public class ExcelConstant {


    /**
     * excel2007扩展名
     */
    public static final String EXCEL07_EXTENSION = ".xlsx";


    /**
     * 每个sheet存储的记录数 100W
     */
    public static final Integer PER_SHEET_ROW_COUNT = 1000000;

    /**
     * 每次向EXCEL写入的记录数(查询每页数据大小) 20W
     */
    public static final Integer PER_WRITE_ROW_COUNT = 200000;


    /**
     * 每个sheet的写入次数 5
     */
    public static final Integer PER_SHEET_WRITE_COUNT = PER_SHEET_ROW_COUNT / PER_WRITE_ROW_COUNT;


    /**
     * 读取excel的时候每次批量插入数据库记录数
     */
    public static final Integer PER_READ_INSERT_BATCH_COUNT = 10000;

}
