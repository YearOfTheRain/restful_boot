package com.restful.system.service.impl;

import com.restful.system.mapper.SysUserRoleMapper;
import com.restful.system.model.SysUserRole;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SysUserServiceImplTest {

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;


    @Test
    public void saveUser() {
    }

    @Test
    public void saveUserRole() {
        Long[] roles = new Long[]{1L, 2L, 3L, 4L};
        Long userId = 4L;
        List<SysUserRole> sysUserRoleList = new ArrayList<>();
        for (Long roleId : roles) {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(userId);
            sysUserRole.setRoleId(roleId);
            sysUserRoleList.add(sysUserRole);
        }
        int i = sysUserRoleMapper.batchUserRole(sysUserRoleList);
        Assert.assertTrue(i > 0);
    }
}