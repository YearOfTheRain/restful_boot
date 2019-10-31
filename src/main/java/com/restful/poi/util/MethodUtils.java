package com.restful.poi.util;

/**
 * @author LiShuLin
 * @version v1.0
 * @program restful_boot
 * @description 获取 properties 的 set 和 get 方法
 * @date 2019-10-08 11:22
 */
public class MethodUtils {

    /*** set 前缀*/
    private static final String SET_PREFIX = "set";
    /*** get 前缀*/
    private static final String GET_PREFIX = "get";

    /**
     * 方法描述: 方法名封装 首字符转大写
     *
     * @param name 待改变目标
     * @return java.lang.String
     * @author LiShuLin
     * @date 2019/10/8
     */
    private static String capitalize(String name) {
        if (name == null || name.length() == 0) {
            return name;
        }
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

    /**
     * 方法描述: set 方法封装
     *
     * @param propertyName 字段名
     * @return java.lang.String
     * @author LiShuLin
     * @date 2019/10/8
     */
    public static String setMethodName(String propertyName) {
        return SET_PREFIX + capitalize(propertyName);
    }

    /**
     * 方法描述: get 方法封装
     *
     * @param propertyName 字段名
     * @return java.lang.String
     * @author LiShuLin
     * @date 2019/10/8
     */
    public static String getMethodName(String propertyName) {
        return GET_PREFIX + capitalize(propertyName);
    }

}
