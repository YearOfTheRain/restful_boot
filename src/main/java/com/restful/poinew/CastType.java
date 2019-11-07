package com.restful.poinew;

import com.restful.poi.model.Excel;
import com.restful.poi.util.MethodUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
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

    @SuppressWarnings("deprecation")
    public static Excel string2Excel(List<String> list) {
        Excel excel = new Excel();
        Class<? extends Excel> aClass = excel.getClass();
        Field[] fields = aClass.getDeclaredFields();

        for(int i = 0, size = list.size(); i < size; i++) {
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

    public static void main(String[] args) {
        String[] s = new String[]{"3595843","602793987325060967","10327063","littletime旗舰店","甘草门市部so","2019/9/1 7:50:00","","","148.2","0","98.8","0","取消","付款前交易关闭","","圆通速递","","郭郭","北京","北京市","丰台区","马家堡街道","马家堡街道丰台区角门13号院7号楼4单元504.","15611020825","","普通订单","","","广州市优升电子商务有限公司","默认圆通,取消预售","","淘宝天猫","7154783","602793987325060967","TA9X-LB07","TA9X-LB078C1130","littletime童装2019春夏新品韩版洋气仙气女童公主纱纱裙连衣裙","杏色;130cm","1","99","99","False","取消"};
        List<String> list = Arrays.asList(s);
        try {
            Excel excel = string2Excel(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
