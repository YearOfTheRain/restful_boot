package com.restful.shiro.filter;

import com.alibaba.fastjson.JSONObject;
import com.restful.common.core.ResponseEntity;
import com.restful.common.enums.resultenum.ResultCode;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * @author LiShuLin
 * @version v1.0
 * @program restful_boot
 * @description 重写 认证不通过或者未登录访问 返回未登录 json 信息
 * @date 2019-09-21 12:22
 */
public class MyAuthenticationFilter extends FormAuthenticationFilter {

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        Subject subject = SecurityUtils.getSubject();

        Object sysUser = subject.getPrincipal();

        if (Objects.equals(sysUser, null)) {
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.setContentType("application/json");
            httpServletResponse.getWriter().write(JSONObject.toJSONString(ResponseEntity.failure(ResultCode.USER_NOT_LOGGED_IN)));
        }
        return false;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        return super.isAccessAllowed(request, response, mappedValue);
    }
}
