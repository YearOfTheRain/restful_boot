package com.restful.shiro.util;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * @author LiShuLin
 * @version v1.0
 * @program restful_boot
 * @description shiro 用于加密密码 工具类
 * @date 2019-09-26 11:28
 */
public class EncryptionUtils {

    /*** hash 算法名称*/
    public static final String HASH_ALGORITHM_NAME = "MD5";
    /*** 密码加密混合盐值*/
    public static final String SALT_KEY = "{$.SB]";
    /*** 迭代次数*/
    public static final Integer HASH_ITERATIONS = 1024;

    /**
     * 方法描述: 混合后获取盐值
     *
     * @param userName 用户名
     * @return java.lang.Object
     * @author LiShuLin
     * @date 2019/9/26
     */
    public static ByteSource getSalt(String userName) {
        return ByteSource.Util.bytes(userName + SALT_KEY);
    }

    /**
     * 方法描述:  获取加密密码
     *
     * @param userName 用户名
     * @param passWord 加密前密码
     * @return java.lang.Object  加密后密码
     * @author LiShuLin
     * @date 2019/9/26
     */
    public static String getEncryptionPassword(String userName, String passWord) {
        return new SimpleHash(HASH_ALGORITHM_NAME, passWord, getSalt(userName), HASH_ITERATIONS).toString();
    }


    /**
     * 我们可以这样获取:盐值加密后的结果
     *
     * @param args
     * @date 2018年8月15日 上午11:46:23
     */
    public static void main(String[] args) {
        System.out.println(getEncryptionPassword("lisi", "123456"));
    }
}
