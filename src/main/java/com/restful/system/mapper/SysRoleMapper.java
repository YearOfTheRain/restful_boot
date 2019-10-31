package com.restful.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.restful.system.model.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author LiShuLin
 * @version v1.0
 * @program restful_boot
 * @description 角色 mapper
 * @date 2019-09-24 15:28
 */
@Repository
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * 方法描述: 根据字段 role_key 查询角色
     * @param roleKey  角色key
     * @return com.restful.system.model.SysRole
     * @author LiShuLin
     * @date 2019/9/25
     */
    @Select("select * from sys_role where role_key = #{roleKey}")
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
