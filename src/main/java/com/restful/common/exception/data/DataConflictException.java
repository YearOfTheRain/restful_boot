package com.restful.common.exception.data;

import com.restful.common.enums.resultenum.ResultCode;
import com.restful.common.exception.BusinessException;

/**
 * @author LiShuLin
 * @version v1.0
 * @program restful_boot
 * @description 数据已存在异常
 * @date 2019-09-18 15:46
 */
public class DataConflictException extends BusinessException {

    private static final long serialVersionUID = -568378862679049403L;

    public DataConflictException() {
    }

    public DataConflictException(Object data) {
        super();
        super.data = data;
    }

    public DataConflictException(String message) {
        super(message);
    }

    public DataConflictException(String format, Object... objects) {
        super(format, objects);
    }

    public DataConflictException(ResultCode resultCode) {
        super(resultCode);
    }

    public DataConflictException(ResultCode resultCode, Object data) {
        super(resultCode, data);
    }
}
