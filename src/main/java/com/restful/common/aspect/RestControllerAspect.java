package com.restful.common.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.restful.common.exception.GlobalExceptionHandler;
import com.restful.common.util.RequestContextHolderUtil;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author LiShuLin
 * @version v1.0
 * @program restful_boot
 * @description 请求参数、响应体统一日志打印
 * @date 2019-09-17 15:03
 */
@Aspect
@Component
public class RestControllerAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 方法名后缀
     */
    private static final String SHORT_METHOD_NAME_SUFFIX = "(..)";

    /**
     * 方法描述: 环绕通知
     *
     * @param joinPoint ProceedingJoinPoint
     * @return java.lang.Object
     * @author LiShuLin
     * @date 2019/9/17
     */
    @Around("@within(org.springframework.web.bind.annotation.RestController) || @annotation(org.springframework.web.bind.annotation.RestController)")
    public Object apiLog(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        boolean logFlag = this.needToLog(method);
        if (!logFlag) {
            return joinPoint.proceed();
        }
        HttpServletRequest request = RequestContextHolderUtil.getHttpServletRequest();
        String methodName = this.getMethodName(joinPoint);
        String params = this.getParamsJson(joinPoint);

        logger.info("Started request method [{}] params [{}]", methodName, params);
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        logger.info("Ended request method [{}] params[{}] response is [{}] cost [{}] millis", methodName, params, this.deleteSensitiveContent(result), System.currentTimeMillis() - start);
        return result;
    }

    /**
     * 方法描述: 获得访问方法名
     *
     * @param joinPoint ProceedingJoinPoint
     * @return java.lang.String
     * @author LiShuLin
     * @date 2019/9/17
     */
    private String getMethodName(ProceedingJoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().toShortString();
        if (methodName.endsWith(SHORT_METHOD_NAME_SUFFIX)) {
            methodName = methodName.substring(0, methodName.length() - SHORT_METHOD_NAME_SUFFIX.length());
        }
        return methodName;
    }

    /**
     * 方法描述: 参数 json 化 并隐藏敏感参数
     *
     * @param joinPoint ProceedingJoinPoint
     * @return java.lang.String
     * @author LiShuLin
     * @date 2019/9/17
     */
    private String getParamsJson(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        StringBuilder stringBuilder = new StringBuilder();
        for (Object arg : args) {
            String paramStr;
            if (arg instanceof HttpServletResponse) {
                paramStr = HttpServletResponse.class.getSimpleName();
            } else if (arg instanceof HttpServletRequest) {
                paramStr = HttpServletRequest.class.getSimpleName();
            } else if (arg instanceof MultipartFile) {
                long size = ((MultipartFile) arg).getSize();
                paramStr = MultipartFile.class.getSimpleName() + "size:" + size;
            } else {
                paramStr = this.deleteSensitiveContent(arg);
            }
            stringBuilder.append(paramStr).append(",");
        }
        return stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString();
    }

    /**
     * 方法描述: 判断是否需要记录日志 对 get 请求不记录日志
     *
     * @param method Method
     * @return boolean
     * @author LiShuLin
     * @date 2019/9/17
     */
    private boolean needToLog(Method method) {
        return method.getAnnotation(GetMapping.class) == null
                && !method.getDeclaringClass().equals(GlobalExceptionHandler.class);
    }

    /**
     * 方法描述: 隐藏敏感参数内容
     *
     * @param object Object
     * @return java.lang.String
     * @author LiShuLin
     * @date 2019/9/17
     */
    private String deleteSensitiveContent(Object object) {
        JSONObject jsonObject = new JSONObject();
        if (!isJSONObject(object) || object instanceof Exception) {
            return "";
        }

        try {
            String param = JSON.toJSONString(object);
            jsonObject = JSONObject.parseObject(param);
            List<String> sensitiveFieldList = this.getSensitiveFieldList();
            for (String sensitiveField : sensitiveFieldList) {
                if (jsonObject.containsKey(sensitiveField)) {
                    jsonObject.put(sensitiveField, "******");
                }
            }
            // 无 data 参数 直接返回
            if(!isJSONObject(jsonObject.get("data"))) {
                return jsonObject.toJSONString();
            }

            //针对 data 中有敏感参数进行隐藏
            JSONObject data = jsonObject.getJSONObject("data");
            if (data != null) {
                sensitiveFieldList.forEach(s -> {
                    if (data.containsKey(s)) {
                        data.put(s, "******");
                    }
                });
                jsonObject.put("data", data);
            }
        } catch (ClassCastException e) {
            return String.valueOf(object);
        }
        return jsonObject.toJSONString();
    }

    /**
     * 方法描述: 敏感字段列表
     *
     * @return java.util.List<java.lang.String>
     * @author LiShuLin
     * @date 2019/9/17
     */
    private List<String> getSensitiveFieldList() {
        List<String> sensitiveFieldList = new ArrayList<>();
        sensitiveFieldList.add("pwd");
        sensitiveFieldList.add("password");
        return sensitiveFieldList;
    }

    /**
     * 方法描述: 判断是否能将入参转为 JSONObject
     *
     * @param object 入参
     * @return boolean 不能--false  能--true
     * @author LiShuLin
     * @date 2019/9/20
     */
    private boolean isJSONObject(Object object) {
        String jsonString = JSONObject.toJSONString(object != null ? object : "");
        // 判空处理 增加空字符串、空格的判断
        if (StringUtils.isEmpty(jsonString) || StringUtils.isBlank(jsonString)) {
            return false;
        }
        try {
            JSONObject.parseObject(jsonString);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
