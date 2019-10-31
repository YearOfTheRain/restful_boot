package com.restful.common.core;

import lombok.Data;

import java.io.Serializable;

/**
 * @author LiShuLin
 * @version v1.0
 * @program restful_boot
 * @description ParameterInvalidItem 字段 valid 校验不通过 返回 model
 * @date 2019-09-17 18:32
 */
@Data
public class ParameterInvalidItem implements Serializable {

    private static final long serialVersionUID = -6170291071802796011L;

    /*** 字段名*/
    private String fieldName;
    /*** 提示信息*/
    private String message;
}
