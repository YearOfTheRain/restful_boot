package com.restful.system.service.impl;

import com.restful.redis.utils.RedisHashMapUtil;
import com.restful.redis.utils.RedisListUtil;
import com.restful.redis.utils.RedisStringUtil;
import com.restful.system.mapper.SysUserRoleMapper;
import com.restful.system.model.SysRole;
import com.restful.system.model.SysUserRole;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SysUserServiceImplTest {

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private RedisStringUtil stringUtil;

    @Autowired
    private RedisListUtil redisListUtil;

    @Autowired
    private RedisHashMapUtil redisHashMapUtil;


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

    @Test
    public void testRedis() {
        List<String> list = new ArrayList<>(10);
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");

        List<SysRole> sysRoles = new ArrayList<>(4);
        for (int i = 0; i < 4; i++) {
            SysRole sysRole = new SysRole();
            sysRole.setRoleKey("key" + i);
            sysRole.setRoleName("name" + i);
            sysRoles.add(sysRole);
        }

        Map<String, Object> hashMap = new HashMap<>(8);
        hashMap.put("key", "key");
        hashMap.put("name", "name");
        hashMap.put("qewq", "keqweqwy");
        hashMap.put("qweq", "keqweqy");
        hashMap.put("qqwe", "qweq");

        // 保存字符串
        long time = 2000;
        stringUtil.set("aaa", "1111", time);
        redisListUtil.set("bbb", list);
        redisListUtil.set("role", sysRoles);
        redisHashMapUtil.set("dsfs", hashMap);
        redisHashMapUtil.set("dsfs", "sdfads", "dsfsd");
        redisHashMapUtil.set("dsfs", "sdfads1", "dsfsd");
        redisHashMapUtil.set("dsfs", "sdfads2", "dsfsd");
        redisHashMapUtil.set("dsfs", "sdfads3", "dsfsd");
    }
}