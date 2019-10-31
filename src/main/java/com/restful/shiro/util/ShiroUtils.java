package com.restful.shiro.util;

import com.restful.system.model.SysUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

/**
 * @author LiShuLin
 * @version v1.0
 * @program restful_boot
 * @description shiro 工具类
 * @date 2019-09-24 14:06
 */
public class ShiroUtils {

    public static Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    public static Session getSession() {
        return getSubject().getSession();
    }

    public static SysUser getSysUser() {
        Object principal = getSubject().getPrincipal();
        return principal != null ? (SysUser)principal : new SysUser() ;
    }
}
