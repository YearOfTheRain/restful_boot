package com.restful.poinew;

import java.util.List;

/**
 * @author Shulin Li
 * @version v1.0
 * @program restful_boot
 * @description 委托类
 * @date 2019-11-06 16:04
 */
public interface ExcelReadDataDelegated {


    /**
     * 每获取一条记录，即写数据
     * 在flume里每获取一条记录即写，而不必缓存起来，可以大大减少内存的消耗，这里主要是针对flume读取大数据量excel来说的
     *
     * @param sheetIndex    sheet位置
     * @param totalRowCount 该sheet总行数
     * @param curRow        行号
     * @param cellList      行数据
     */
    void readExcelDate(int sheetIndex, int totalRowCount, int curRow, List<String> cellList);
}
