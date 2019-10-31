package com.restful.common.core;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author LiShuLin
 * @version v1.0
 * @program my-boot
 * @description 顶层的 model
 * @date 2019-09-10 11:18
 */
@Getter
@Setter
@ToString
public class BaseEntity {

    /*** 主键*/
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /*** 创建时间*/
    private Date createTime;

    /*** 更新时间(最近一次更新时间 )*/
    private Date updateTime;

    /*** 删除标志 默认 0, 0-正常, 1-删除*/
    @TableField("delete_flag")
    @TableLogic
    @JsonIgnore
    private int deleteFlag;

}
