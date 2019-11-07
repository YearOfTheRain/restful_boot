package com.restful.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.restful.shiro.util.EncryptionUtils;
import com.restful.system.mapper.SysUserMapper;
import com.restful.system.mapper.SysUserRoleMapper;
import com.restful.system.model.SysUser;
import com.restful.system.model.SysUserRole;
import com.restful.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author LiShuLin
 * @version v1.0
 * @program restful_boot
 * @description 用户信息接口实现
 * @date 2019-09-17 14:46
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    /**
     * 方法描述: 根据用户名查询用户信息
     *
     * @param name 用户名
     * @return com.restful.system.model.SysUser
     * @author LiShuLin
     * @date 2019/9/24
     */
    @Override
    public SysUser selectByName(String name) {
        return baseMapper.selectByName(name);
    }

    /**
     * 方法描述: 自定义新增方法
     *
     * @param sysUser 用户对象
     * @return boolean
     * @author LiShuLin
     * @date 2019/9/26
     */
    @Override
    public int saveUser(SysUser sysUser) {
        sysUser.setCreateTime(new Date());
        sysUser.setPassword(EncryptionUtils.getEncryptionPassword(sysUser.getUserName(), sysUser.getPassword()));
        int result = baseMapper.insert(sysUser);
        insertUserRole(sysUser);
        return result;
    }


    /**
     * 方法描述:  批量新增 用户角色关系
     *
     * @param sysUser 用户对象
     * @author LiShuLin
     * @date 2019/9/27
     */
    @Override
    public void insertUserRole(SysUser sysUser) {
        if (sysUser.getRoles().length > 0) {
            Long[] roles = sysUser.getRoles();
            List<SysUserRole> sysUserRoleList = new ArrayList<>();
            for (Long roleId : roles) {
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setUserId(sysUser.getId());
                sysUserRole.setRoleId(roleId);
                sysUserRoleList.add(sysUserRole);
            }
            userRoleMapper.batchUserRole(sysUserRoleList);
        }
    }

}
