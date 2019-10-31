package com.restful.system.web.controller;

import com.restful.common.core.ResponseEntity;
import com.restful.common.exception.data.DataConflictException;
import com.restful.system.model.SysRole;
import com.restful.system.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

/**
 * @author LiShuLin
 * @version v1.0
 * @program restful_boot
 * @description 角色 控制层
 * @date 2019-09-24 16:52
 */
@RestController
@RequestMapping("/api/sys_role")
public class SysRoleController {

    @Autowired
    private ISysRoleService sysRoleService;

    /**
     * 方法描述: 新增方法
     *
     * @param sysRole 角色对象
     * @return com.restful.common.core.ResponseEntity
     * @author LiShuLin
     * @date 2019/9/25
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity add(@Valid @RequestBody SysRole sysRole) {
        sysRole.setCreateTime(new Date());
        try {
            sysRoleService.save(sysRole);
        } catch (Exception e) {
            throw new DataConflictException(sysRole);
        }
        return ResponseEntity.success(sysRole);
    }

    /**
     * 方法描述: 更新方法
     *
     * @param sysRole 角色对象
     * @return com.restful.common.core.ResponseEntity
     * @author LiShuLin
     * @date 2019/9/25
     */
    @PutMapping()
    public ResponseEntity update(@Valid @RequestBody SysRole sysRole) {
        sysRole.setUpdateTime(new Date());
        try {
            sysRoleService.saveOrUpdate(sysRole);
        } catch (Exception e) {
            throw new DataConflictException(sysRole);
        }
        return ResponseEntity.success(sysRole);
    }

    /**
     * 方法描述: role_key 字段 唯一校验
     *
     * @param roleKey 角色 key
     * @return com.restful.common.core.ResponseEntity
     * @author LiShuLin
     * @date 2019/9/25
     */
    @GetMapping("/check_role_key/{roleKey}")
    public ResponseEntity checkRoleKey(@PathVariable("roleKey") String roleKey) {
        return ResponseEntity.success(sysRoleService.selectByRoleKey(roleKey));
    }

    /**
     * 方法描述:  根据 id 获取角色信息
     *
     * @param id 角色 id
     * @return com.restful.common.core.ResponseEntity
     * @author LiShuLin
     * @date 2019/9/25
     */
    @GetMapping("/{id}")
    public ResponseEntity selectById(@PathVariable("id") Integer id) {
        return ResponseEntity.success(sysRoleService.getById(id));
    }

}
