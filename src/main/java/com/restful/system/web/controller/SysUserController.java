package com.restful.system.web.controller;

import com.restful.common.core.ResponseEntity;
import com.restful.common.exception.data.DataConflictException;
import com.restful.system.model.SysUser;
import com.restful.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author LiShuLin
 * @version v1.0
 * @program restful_boot
 * @description 用户信息控制层
 * @date 2019-09-17 14:48
 */
@RestController
@RequestMapping("/api/sys_user")
public class SysUserController {

    @Autowired
    private ISysUserService sysUserService;

    /**
     * 方法描述: 全查方法
     *
     * @return com.restful.common.core.ResponseEntity
     * @author LiShuLin
     * @date 2019/9/18
     */
    @GetMapping
    public ResponseEntity getAll() {
        List<SysUser> list = sysUserService.list();
        return ResponseEntity.success(list);
    }

    /**
     * 方法描述: 新增方法
     *
     * @param sysUser 用户信息对象
     * @return com.restful.common.core.ResponseEntity
     * @author LiShuLin
     * @date 2019/9/17
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity add( @RequestBody SysUser sysUser) {
        try {
            sysUserService.saveUser(sysUser);
        } catch (Exception e) {
            throw new DataConflictException(sysUser);
        }
        return ResponseEntity.success(sysUser);
    }

    @PostMapping("/test")
    public ResponseEntity test(String name) {
        return ResponseEntity.success(name);
    }


}
