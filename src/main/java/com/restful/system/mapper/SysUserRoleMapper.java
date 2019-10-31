package com.restful.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.restful.system.model.SysUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author LiShuLin
 * @version v1.0
 * @program restful_boot
 * @description 用户角色 mapper
 * @date 2019-09-24 16:09
 */
@Repository
@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {
    
    /** 
     * 方法描述: 批量新增 用户角色关联信息 
     *
     * @param userRoleList 用户角色 List 
     * @return int 
     * @author LiShuLin
     * @date 2019/9/25 
     */
    int batchUserRole(List<SysUserRole> userRoleList);

    /**
     * 方法描述: 删除用户绑定的角色
     *
     * @param userId 用户 id
     * @return int
     * @author LiShuLin
     * @date 2019/9/26
     */
    int deleteUserRole(Long userId);
}
