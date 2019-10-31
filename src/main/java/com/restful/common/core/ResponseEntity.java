package com.restful.common.core;

import com.restful.common.enums.resultenum.ResultCode;
import lombok.Data;

import java.io.Serializable;

/**
 * @author LiShuLin
 * @version v1.0
 * @program restful_boot
 * @description 返回 entity
 * @date 2019-09-16 16:01
 */
@Data
public class ResponseEntity implements Serializable {

    private static final long serialVersionUID = 2468095189493036616L;

    /*** 状态码*/
    private Integer code;
    /*** 信息*/
    private String msg;
    /*** 数据*/
    private Object data;

    /**
     * 方法描述: 无参构造方法
     * @author LiShuLin
     * @date 2019/9/17
     */
    private ResponseEntity() {
    }

    /**
     * 方法描述: 带参构造方法
     * @param code  Integer code
     * @param msg String msg
     * @author LiShuLin
     * @date 2019/9/17
     */
    public ResponseEntity(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 方法描述: 成功
     * @return com.restful.common.core.ResponseEntity
     * @author LiShuLin
     * @date 2019/9/17
     */
    public static ResponseEntity success() {
        ResponseEntity responseEntity = new ResponseEntity();
        responseEntity.setResultCode(ResultCode.SUCCESS);
        return responseEntity;
    }

    /**
     * 方法描述: 带 data 的成功
     * @param data Object data
     * @return com.restful.common.core.ResponseEntity
     * @author LiShuLin
     * @date 2019/9/17
     */
    public static ResponseEntity success(Object data) {
        ResponseEntity responseEntity = new ResponseEntity();
        responseEntity.setResultCode(ResultCode.SUCCESS);
        responseEntity.setData(data);
        return responseEntity;
    }

    /**
     * 方法描述: 带错误码的失败
     * @param code 枚举类 ResultCode
     * @return com.restful.common.core.ResponseEntity
     * @author LiShuLin
     * @date 2019/9/17
     */
    public static ResponseEntity failure(ResultCode code) {
        ResponseEntity responseEntity = new ResponseEntity();
        responseEntity.setResultCode(code);
        return responseEntity;
    }

    /**
     * 方法描述: 带错误码和错误 data 的失败
     * @param code 枚举类 ResultCode
     * @param data Object data
     * @return com.restful.common.core.ResponseEntity
     * @author LiShuLin
     * @date 2019/9/17
     */
    public static ResponseEntity failure(ResultCode code, Object data) {
        ResponseEntity responseEntity = new ResponseEntity();
        responseEntity.setResultCode(code);
        responseEntity.setData(data);
        return responseEntity;
    }

    /**
     * 方法描述: 根据枚举类 ResultCode 设置 code 和 msg 的值
     * @param code 枚举类 ResultCode
     * @return void
     * @author LiShuLin
     * @date 2019/9/17
     */
    private void setResultCode(ResultCode code) {
        this.code = code.code();
        this.msg = code.message();
    }
}
