package com.restful.system.web.controller;

import com.restful.common.core.ResponseEntity;
import com.restful.common.enums.resultenum.ResultCode;
import com.restful.system.vo.LoginFormVO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

/**
 * @author LiShuLin
 * @version v1.0
 * @program restful_boot
 * @description 登录控制器
 * @date 2019-09-20 18:46
 */
@Controller
public class LoginController {

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity login(@Valid @RequestBody LoginFormVO loginFormVO) {
        Subject currentUser = SecurityUtils.getSubject();

        if (currentUser.isAuthenticated()) {
            return ResponseEntity.success(currentUser.getPrincipal());
        }
        UsernamePasswordToken token = new UsernamePasswordToken(loginFormVO.getUserName(), loginFormVO.getPassword());
        //包装类 不带默认值 前端未传时 默认使用 false
        Boolean rememberMe = loginFormVO.getRememberMe();
        token.setRememberMe(rememberMe != null ? rememberMe : false);
        try {
            currentUser.login(token);
        } catch (AuthenticationException e) {
            return ResponseEntity.failure(ResultCode.USER_NOT_LOGGED_IN);
        }
        return ResponseEntity.success(currentUser.getPrincipal());
    }

    @GetMapping("/")
    public String index() {
        return "/index.html";
    }
}
