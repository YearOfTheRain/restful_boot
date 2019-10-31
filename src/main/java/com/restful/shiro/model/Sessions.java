package com.restful.shiro.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author LiShuLin
 * @version v1.0
 * @program restful_boot
 * @description 持久化 session
 * @date 2019-09-19 12:10
 */
@Data
@TableName("sessions")
@Deprecated
public class Sessions implements Serializable {

    private static final long serialVersionUID = 7017147971247526333L;

    /*** 主键*/
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /*** session key 值*/
    private String jsessionid;

    /*** session 值*/
    private String session;

    /*** 创建时间*/
    private Date createTime;

}
