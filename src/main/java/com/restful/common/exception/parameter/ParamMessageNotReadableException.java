package com.restful.common.exception.parameter;

import com.restful.common.enums.resultenum.ResultCode;
import com.restful.common.exception.BusinessException;

/**
 * @author LiShuLin
 * @version v1.0
 * @program restful_boot
 * @description 参数消息不可读异常
 * @date 2019-09-20 17:34
 */
@Deprecated
public class ParamMessageNotReadableException extends BusinessException {

    public ParamMessageNotReadableException() {
    }

    public ParamMessageNotReadableException(Object data) {
        super();
        super.data = data;
    }

    public ParamMessageNotReadableException(String message) {
        super(message);
    }

    public ParamMessageNotReadableException(String format, Object... objects) {
        super(format, objects);
    }

    public ParamMessageNotReadableException(ResultCode resultCode) {
        super(resultCode);
    }

    public ParamMessageNotReadableException(ResultCode resultCode, Object data) {
        super(resultCode, data);
    }
}
