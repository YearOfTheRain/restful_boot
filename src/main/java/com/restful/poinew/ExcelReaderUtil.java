package com.restful.poinew;

import com.restful.poi.constant.ExcelConstansts;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

import static com.restful.poi.util.PoiUtils.DEFAULT_NUM_VALUE;

/**
 * @author Shulin Li
 * @version v1.0
 * @program restful_boot
 * @description 读取工具类
 * @date 2019-11-06 16:05
 */
@Slf4j
public class ExcelReaderUtil {


    public static void readExcel(String filePath, ExcelReadDataDelegated excelReadDataDelegated) throws Exception {

        if (filePath.endsWith(ExcelConstant.EXCEL07_EXTENSION)) {
            ExcelReader reader = new ExcelReader(excelReadDataDelegated);
            reader.process(filePath);
        } else {
            throw new Exception("文件格式错误，fileName的扩展名只能是xlsx!");
        }
    }

    public static void readExcel(InputStream inputStream, ExcelReadDataDelegated excelReadDataDelegated) throws Exception {
        try {
            ExcelReader reader = new ExcelReader(excelReadDataDelegated);
            reader.process(inputStream);
        } catch (Exception e) {
            throw new Exception("文件格式错误，fileName的扩展名只能是xlsx!");
        }
    }

    /**
     * 类型转换
     */
    public static Object convertType(String classType, String value) {
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


    public static void main(String[] args) throws Exception {
        String path = "D:\\小程序ERP导入字段has same.xlsx";
        /*ExcelReaderUtil.readExcel(path, (int sheetIndex, int totalRowCount, int curRow, List<String> cellList) -> {
            try {
                SpringUtils.getBean(IExcelService.class).saveOrUpdate(CastType.string2Excel(cellList));
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                log.error("类型转换失败，该条数据未入库", e);
            }
        });*/
        File file = new File(path);
        ExcelReaderUtil.readExcel(new FileInputStream(file), (int sheetIndex, int totalRowCount, int curRow, List<String> cellList) -> {
            System.out.println("总行数为：" + totalRowCount + " 行号为：" + curRow + " 数据：" + cellList);
        });
    }
}
