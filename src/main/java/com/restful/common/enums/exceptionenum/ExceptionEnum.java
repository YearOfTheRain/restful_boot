package com.restful.common.enums.exceptionenum;

import com.restful.common.enums.resultenum.ResultCode;
import com.restful.common.exception.BusinessException;
import com.restful.common.exception.data.DataConflictException;
import com.restful.common.exception.data.DataNotFoundException;
import org.springframework.http.HttpStatus;

import java.util.Objects;

/**
 * @author LiShuLin
 * @version v1.0
 * @program restful_boot
 * @description 自定义异常枚举类
 * @date 2019-09-18 14:34
 */
public enum ExceptionEnum {

    /*** 数据未找到*/
    NOT_FOUND(DataNotFoundException.class, HttpStatus.NOT_FOUND, ResultCode.RESULT_DATA_NONE),

    /*** 数据已存在*/
    CONFLICT(DataConflictException.class, HttpStatus.CONFLICT, ResultCode.DATA_ALREADY_EXISTED);


    private Class<? extends BusinessException> eClass;
    private HttpStatus httpStatus;
    private ResultCode resultCode;

    ExceptionEnum(Class<? extends BusinessException> eClass, HttpStatus httpStatus, ResultCode resultCode) {
        this.eClass = eClass;
        this.httpStatus = httpStatus;
        this.resultCode = resultCode;
    }

    public Class<? extends BusinessException> geteclass() {
        return eClass;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }

    /**
     * 方法描述: 根据 HttpStatus 获得对应返回信息
     *
     * @param httpStatus HttpStatus
     * @return com.restful.common.enums.exceptionenum.ExceptionEnum
     * @author LiShuLin
     * @date 2019/9/18
     */
    public static ExceptionEnum getByHttpStatus(HttpStatus httpStatus) {
        for (ExceptionEnum exceptionEnum : ExceptionEnum.values()) {
            if (Objects.equals(httpStatus, exceptionEnum.httpStatus)) {
                return exceptionEnum;
            }
        }
        return null;
    }

    /**
     * 方法描述: 根据异常类，获取对应的返回信息
     *
     * @param eClass 异常
     * @return com.restful.common.enums.exceptionenum.ExceptionEnum
     * @author LiShuLin
     * @date 2019/9/18
     */
    public static ExceptionEnum getByEClass(Class<? extends BusinessException> eClass) {
        for (ExceptionEnum exceptionEnum : ExceptionEnum.values()) {
            if (Objects.equals(eClass, exceptionEnum.eClass)) {
                return exceptionEnum;
            }
        }
        return null;
    }

}
