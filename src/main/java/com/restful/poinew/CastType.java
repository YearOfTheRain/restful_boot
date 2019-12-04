package com.restful.poinew;

import com.restful.poi.model.Excel;
import com.restful.poi.util.MethodUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

/**
 * @author Shulin Li
 * @version v1.0
 * @program restful_boot
 * @description 类型转换
 * @date 2019-11-07 12:26
 */
public class CastType {

    public static Excel string2Excel(List<String> list) {

        Excel excel = new Excel();
        Class<? extends Excel> aClass = excel.getClass();
        Field[] fields = aClass.getDeclaredFields();

        for (int i = 0, size = list.size(); i < size; i++) {
            methodInvoke(list.get(i), excel, fields[i]);
        }
        return excel;
    }

    public static void methodInvoke(String str, Excel excel, Field field) {
        String methodName = MethodUtils.setMethodName(field.getName());
        try {
            Method method = Excel.class.getMethod(methodName, field.getType());
            if (Date.class == field.getType()) {
                Date date = null;
                if (!StringUtils.isEmpty(str)) {
                    date = new Date(str);
                }
                method.invoke(excel, date);
            } else {
                method.invoke(excel, ExcelReaderUtil.convertType(field.getType().getName(), str));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
