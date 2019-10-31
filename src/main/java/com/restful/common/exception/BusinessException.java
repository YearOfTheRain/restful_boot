package com.restful.common.exception;

import com.restful.common.enums.exceptionenum.ExceptionEnum;
import com.restful.common.enums.resultenum.ResultCode;
import lombok.Data;
import org.springframework.util.StringUtils;

/**
 * @author LiShuLin
 * @version v1.0
 * @program restful_boot
 * @description 业务异常
 * @date 2019-09-18 14:28
 */
@Data
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = -4620087923060000981L;

    protected Integer code;

    protected String message;

    protected ResultCode resultCode;

    protected Object data;

    public BusinessException() {
        ExceptionEnum exceptionEnum = ExceptionEnum.getByEClass(this.getClass());
        if (exceptionEnum != null) {
            resultCode = exceptionEnum.getResultCode();
            code = exceptionEnum.getResultCode().code();
            message = exceptionEnum.getResultCode().message();
        }
    }

    public BusinessException(String message) {
        this();
        this.message = message;
    }

    /**
     * 方法描述: 根据传来的格式，格式化 message 信息
     * @param format 展示格式
     * @param objects 展示的数据
     * @author LiShuLin
     * @date 2019/9/18
     */
    public BusinessException(String format, Object... objects) {
        this();
        format = StringUtils.replace(format, "{}", "%s");
        this.message = String.format(format, objects);
    }

    public BusinessException(ResultCode resultCode) {
        this.resultCode = resultCode;
        this.code = resultCode.code();
        this.message = resultCode.message();
    }

    public BusinessException(ResultCode resultCode, Object data) {
        this(resultCode);
        this.data = data;
    }
}
