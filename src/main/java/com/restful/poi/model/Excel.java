package com.restful.poi.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author LiShuLin
 * @version v1.0
 * @program restful_boot
 * @description excel 测试类
 * @date 2019-09-28 14:18
 */
@Data
@TableName("excel")
public class Excel {

    @TableId(value = "order_id", type = IdType.INPUT)
    private String orderId;
    private String onlineOrderId;
    private String shopId;
    private String shopName;
    private String buyerName;
    private Date orderTime;
    private Date buyTime;
    private Date sendTime;
    private Double shouldPay;
    private Double hasPay;
    private Double discountPay;
    private Double fare;
    private String status;
    private String shopStatus;
    private String errorType;
    private String courierCompany;
    private String trackingNumber;
    private String consigneeName;
    private String province;
    private String city;
    private String district;
    private String street;
    private String address;
    private String mobilePhone;
    private String fixedTelephone;
    private String orderType;
    private String buyerMessage;
    private String orderRemark;
    private String sendStorehouse;
    private String tag;
    private String payNumber;
    private String platform;
    private String childOrderNumber;
    private String originalOnlineOrder;
    private String styleNumber;
    private String produceNumber;
    private String produceName;
    private String colorAndFormat;
    private Long amount;
    private Double producePrice;
    private Double produceMoney;
    private String gift;
    private String childOrderStatus;
    private Double costPrice;
    private Integer resendAmount;
    private Integer resendAmountReal;
    private Double resendMoney;
}
