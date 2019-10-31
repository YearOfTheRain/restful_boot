package com.restful.system.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.restful.common.core.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author LiShuLin
 * @version v1.0
 * @program restful_boot
 * @description 角色信息表
 * @date 2019-09-24 14:56
 */
@Getter
@Setter
@ToString
@TableName("sys_role")
public class SysRole extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -2599531629132443368L;

    /*** 角色名*/
    @NotBlank(message = "角色名不能为空")
    private String roleName;

    /*** 角色 key */
    @NotBlank(message = "角色 key 不能为空")
    private String roleKey;

    /*** 状态标志 默认 0, 0-启用, 1-禁用*/
    private Integer status;

    /*** 备注*/
    private String remark;

}
