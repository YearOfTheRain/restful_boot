package com.restful.system.service.impl;

import com.restful.system.model.SysRole;
import com.restful.system.service.ISysRoleService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SysRoleServiceImplTest {

    @Autowired
    private ISysRoleService sysRoleService;

    private List<SysRole> sysRoleList;

    private int i;

    public SysRole buildSysRole() {
        SysRole sysRole = new SysRole();
        sysRole.setRoleName("qwer" + ++i);
        sysRole.setRoleKey("key" + ++i);
        return sysRole;
    }

    @Before
    public void buildList() {
        sysRoleList = new ArrayList<>();
        for (int index = 0; index < 5; index++) {
            sysRoleList.add(buildSysRole());
        }
    }

    @Test
    public void selectRoleByUserId() {
        Long userId = 1L;
        List<String> roleKeyList = sysRoleService.selectRoleByUserId(userId);
        Assert.assertTrue(roleKeyList.size() > 0);
        roleKeyList.forEach(roleKey -> {
            System.out.println(roleKey);
        });
    }

    @Test
    public void test1() {
        List<String> list = new ArrayList<>();
        list.add("aaa bbb ccc");
        list.add("ddd eee fff");
        list.add("ggg hhh iii");

        list = list.stream().map(s -> s.split(" ")).flatMap(Arrays::stream).collect(toList());
        list.forEach(System.out::println);
    }

    @Test
    public void test2() {
        sysRoleList.forEach(System.out::println);
        List<String> newlist = sysRoleList.stream().map(SysRole::getRoleName).collect(toList());
        newlist.forEach(System.out::println);
    }
}