package com.restful.common.exception.data;

import com.restful.common.enums.resultenum.ResultCode;
import com.restful.common.exception.BusinessException;

/**
 * @author LiShuLin
 * @version v1.0
 * @program restful_boot
 * @description 数据未找到异常
 * @date 2019-09-18 15:44
 */
public class DataNotFoundException extends BusinessException {

    private static final long serialVersionUID = -7881333973590802163L;

    public DataNotFoundException() {
    }

    public DataNotFoundException(Object data) {
        super();
        super.data = data;
    }

    public DataNotFoundException(String message) {
        super(message);
    }

    public DataNotFoundException(String format, Object... objects) {
        super(format, objects);
    }

    public DataNotFoundException(ResultCode resultCode) {
        super(resultCode);
    }

    public DataNotFoundException(ResultCode resultCode, Object data) {
        super(resultCode, data);
    }
}
