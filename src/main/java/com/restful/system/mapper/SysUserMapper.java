package com.restful.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.restful.system.model.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @author LiShuLin
 * @version v1.0
 * @program restful_boot
 * @description SysUserMapper
 * @date 2019-09-17 14:42
 */
@Repository
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 方法描述: 根据用户名查询用户信息
     * @param name 用户名
     * @return com.restful.system.model.SysUser
     * @author LiShuLin
     * @date 2019/9/24
     */
    @Select("select * from sys_user where user_name = #{name} and delete_flag = 0")
    SysUser selectByName(String name);
}
