package com.restful.common.exception.service;

import com.restful.common.enums.resultenum.ResultCode;
import com.restful.common.exception.BusinessException;

/**
 * @author LiShuLin
 * @version v1.0
 * @program restful_boot
 * @description service 异常
 * @date 2019-09-26 15:37
 */
public class ServiceException extends BusinessException {

    public ServiceException() {
        super();
    }

    public ServiceException(Object data) {
        super();
        super.data = data;
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String format, Object... objects) {
        super(format, objects);
    }

    public ServiceException(ResultCode resultCode) {
        super(resultCode);
    }

    public ServiceException(ResultCode resultCode, Object data) {
        super(resultCode, data);
    }
}
