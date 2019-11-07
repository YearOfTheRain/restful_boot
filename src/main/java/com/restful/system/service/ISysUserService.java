package com.restful.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.restful.system.model.SysUser;

/**
 * @author LiShuLin
 * @version v1.0
 * @program restful_boot
 * @description 用户信息接口
 * @date 2019-09-17 14:44
 */
public interface ISysUserService extends IService<SysUser> {

    /**
     * 方法描述: 根据用户名查询用户信息
     *
     * @param name 用户名
     * @return com.restful.system.model.SysUser
     * @author LiShuLin
     * @date 2019/9/24
     */
    SysUser selectByName(String name);

    /**
     * 方法描述: 自定义新增方法
     *
     * @param sysUser 用户对象
     * @return boolean
     * @author LiShuLin
     * @date 2019/9/26
     */
    int saveUser(SysUser sysUser);

    /**
     * 方法描述:  批量新增 用户角色关系
     *
     * @param sysUser 用户对象
     * @author LiShuLin
     * @date 2019/9/27
     */
    void insertUserRole(SysUser sysUser);
}
