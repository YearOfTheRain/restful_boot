package com.restful.poi.model;

import lombok.Data;

/**
 * @author LiShuLin
 * @version v1.0
 * @program restful_boot
 * @description send mail model
 * @date 2019-10-08 10:44
 */
@Data
public class Mail {

    /*** 发件人*/
    private String form;

    /*** 收件人*/
    private String to;

    /*** 主题文字*/
    private String subject;

    /*** 文本消息*/
    private String text;

    /*** 文件路径*/
    private String fileName;

}
