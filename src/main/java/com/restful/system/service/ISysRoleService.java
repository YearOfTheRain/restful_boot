package com.restful.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.restful.system.model.SysRole;

import java.util.List;

/**
 * @author LiShuLin
 * @version v1.0
 * @program restful_boot
 * @description 角色 接口
 * @date 2019-09-24 15:30
 */
public interface ISysRoleService extends IService<SysRole> {

    /**
     * 方法描述: 根据字段 role_key 查询角色
     *
     * @param roleKey 角色key
     * @return com.restful.system.model.SysRole
     * @author LiShuLin
     * @date 2019/9/25
     */
    SysRole selectByRoleKey(String roleKey);

    /**
     * 方法描述:  根据用户 id 查询该用户所拥有的角色
     *
     * @param userId 用户 id
     * @return java.util.List<java.lang.String>
     * @author LiShuLin
     * @date 2019/9/27
     */
    List<String> selectRoleByUserId(Long userId);
}
