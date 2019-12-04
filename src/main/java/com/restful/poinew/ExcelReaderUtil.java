package com.restful.poinew;

import com.restful.poi.constant.ExcelConstansts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    private static final Map<String, String> NUMBER_TYPE = new HashMap<>(16);
    static {
        NUMBER_TYPE.put(ExcelConstansts.INTEGER_TYPE,"integer");
        NUMBER_TYPE.put(ExcelConstansts.SHORT_TYPE,"short");
        NUMBER_TYPE.put(ExcelConstansts.BYTE_TYPE,"byte");
        NUMBER_TYPE.put(ExcelConstansts.CHAR_TYPE,"char");
        NUMBER_TYPE.put(ExcelConstansts.LONG_TYPE,"long");
        NUMBER_TYPE.put(ExcelConstansts.FLOAT_TYPE,"float");
        NUMBER_TYPE.put(ExcelConstansts.DOUBLE_TYPE,"double");
        NUMBER_TYPE.put(ExcelConstansts.BIG_DECIMAL_TYPE,"bigDecimal");
    }


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
    public static Object convertType(String filedType, String value) {

        // 首先判断字段类型是不是数字类型
        if (containNumberType(filedType)) {
            if (!isNum(value)) {
                value = DEFAULT_NUM_VALUE;
            }
            if (ExcelConstansts.INTEGER_TYPE.equals(filedType)) {
                return Integer.valueOf(value);
            }
            if (ExcelConstansts.LONG_TYPE.equals(filedType)) {
                return Long.valueOf(value);
            }
            if (ExcelConstansts.FLOAT_TYPE.equals(filedType)) {
                return Float.valueOf(value);
            }
            if (ExcelConstansts.DOUBLE_TYPE.equals(filedType)) {
                return Double.valueOf(value);
            }
            //后面这些是 Excel 不涉及的类型，可以放后面
            if (ExcelConstansts.SHORT_TYPE.equals(filedType)) {
                return Short.valueOf(value);
            }
            if (ExcelConstansts.BYTE_TYPE.equals(filedType)) {
                return Byte.valueOf(value);
            }
            if (ExcelConstansts.CHAR_TYPE.equals(filedType)) {
                return value.charAt(0);
            }
            if (ExcelConstansts.BIG_DECIMAL_TYPE.equals(filedType)) {
                return new BigDecimal(value);
            }
        }
        //处理布尔类型
        if (ExcelConstansts.BOOLEAN_TYPE.equals(filedType)) {
            return Boolean.valueOf(value.toLowerCase());
        }

        return value;
    }

    /**
     * 方法描述:  LocalDateTime 转为 Date
     *
     * @param dateTime LocalDateTime
     * @return java.util.Date
     * @author Shulin Li
     * @date 2019/11/18
     */
    public static Date localDateTime2Date(LocalDateTime dateTime) {
        ZonedDateTime zonedDateTime = dateTime.atZone(ZoneId.systemDefault());
        return Date.from(zonedDateTime.toInstant());
    }

    /**
     * 判断一个字符串是否能转化为数字方法
     * @param str 字符串
     * @return 可以转数字 ? true : false
     */
    public static boolean isNum(String str){
        //这里对空字符串做处理
        if (StringUtils.isBlank(str)) {
            return false;
        }
        return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
    }

    /**
     * 判断字段类型是否是数字类型
     * @param filedType 字段类型
     * @return 是数字类型  ? true : false
     */
    public static boolean containNumberType(String filedType) {
        return NUMBER_TYPE.containsKey(filedType);
    }


    public static void main(String[] args) throws Exception {
        String s = "";
        System.out.println(isNum(s));
    }
}
