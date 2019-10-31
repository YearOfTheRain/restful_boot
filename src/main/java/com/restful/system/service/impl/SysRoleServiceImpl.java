package com.restful.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.restful.system.mapper.SysRoleMapper;
import com.restful.system.model.SysRole;
import com.restful.system.service.ISysRoleService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author LiShuLin
 * @version v1.0
 * @program restful_boot
 * @description 角色 接口实现
 * @date 2019-09-24 15:31
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    /**
     * 方法描述: 根据字段 role_key 查询角色
     *
     * @param roleKey 角色key
     * @return com.restful.system.model.SysRole
     * @author LiShuLin
     * @date 2019/9/25
     */
    @Override
    public SysRole selectByRoleKey(String roleKey) {
        return baseMapper.selectByRoleKey(roleKey);
    }

    /**
     * 方法描述:  根据用户 id 查询该用户所拥有的角色
     *
     * @param userId 用户 id
     * @return java.util.List<java.lang.String>
     * @author LiShuLin
     * @date 2019/9/27
     */
    @Override
    public List<String> selectRoleByUserId(Long userId) {
        return baseMapper.selectRoleByUserId(userId);
    }
}
