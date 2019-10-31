package com.restful.system.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.restful.common.core.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author LiShuLin
 * @version v1.0
 * @program my-boot
 * @description 用户信息表
 * @date 2019-09-10 11:16
 */
@Getter
@Setter
@ToString
@TableName("sys_user")
public class SysUser extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -7829697904113885045L;

    /*** 账号*/
    @NotBlank(message = "账号不能为空")
    private String userName;

    /*** 密码*/
    @NotBlank(message = "密码不能为空")
    private String password;

    /*** 真实姓名*/
    @NotBlank(message = "真实姓名不能为空")
    private String actualName;

    @TableField(exist = false)
    private Long[] roles;

    /*** 隐藏 password 传到前端并且也能接受到前端传值*/
    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    /*** 隐藏 roles 传到前端并且也能接受到前端传值*/
    @JsonIgnore
    public Long[] getRoles() {
        return roles;
    }

    @JsonProperty
    public void setRoles(Long[] roles) {
        this.roles = roles;
    }
}
