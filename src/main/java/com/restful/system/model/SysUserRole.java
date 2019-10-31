package com.restful.system.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author LiShuLin
 * @version v1.0
 * @program restful_boot
 * @description 用户角色关联表
 * @date 2019-09-24 16:02
 */
@Data
@TableName("sys_user_role")
public class SysUserRole implements Serializable {

    private static final long serialVersionUID = -1833912157230941379L;

    /*** 用户编号*/
    @NotBlank
    private Long userId;

    /*** 角色编号*/
    @NotBlank
    private Long roleId;
}
