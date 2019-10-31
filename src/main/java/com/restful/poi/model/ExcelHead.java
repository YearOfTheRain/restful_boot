package com.restful.poi.model;

import lombok.Data;

/**
 * @author LiShuLin
 * @version v1.0
 * @program restful_boot
 * @description 中文表格头兼容
 * @date 2019-09-28 14:24
 */
@Data
public class ExcelHead {

    /*** excel 列名*/
    private String excelName;
    /*** 对应 entity 属性名*/
    private String entityName;
    /*** 是否必填*/
    private boolean required = false;

    public ExcelHead() {
    }

    public ExcelHead(String excelName, String entityName) {
        this.excelName = excelName;
        this.entityName = entityName;
    }

    public ExcelHead(String excelName, String entityName, boolean required) {
        this.excelName = excelName;
        this.entityName = entityName;
        this.required = required;
    }
}
