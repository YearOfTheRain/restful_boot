package com.restful.system.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author LiShuLin
 * @version v1.0
 * @program restful_boot
 * @description 登录表单 vo
 * @date 2019-09-20 18:48
 */
@Data
public class LoginFormVO {

    /*** 账号*/
    @NotBlank(message = "账号不能为空")
    private String userName;

    /*** 密码*/
    @NotBlank(message = "密码不能为空")
    private String password;

    /*** 记住我*/
    private Boolean rememberMe;


}
