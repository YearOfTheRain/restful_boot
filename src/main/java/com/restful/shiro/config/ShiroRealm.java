package com.restful.shiro.config;

import com.restful.shiro.util.EncryptionUtils;
import com.restful.system.model.SysUser;
import com.restful.system.service.ISysRoleService;
import com.restful.system.service.ISysUserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;

/**
 * @author LiShuLin
 * @version v1.0
 * @program restful_boot
 * @description 自定义 realm
 * @date 2019-09-19 16:31
 */
@Configuration
public class ShiroRealm extends AuthorizingRealm {

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private ISysRoleService sysRoleService;

    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SysUser user = (SysUser) principals.getPrimaryPrincipal();
        Set<String> roleSet = new HashSet<>(sysRoleService.selectRoleByUserId(user.getId()));

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.addRoles(roleSet);
        return authorizationInfo;
    }

    /**
     * 认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        String username = usernamePasswordToken.getUsername();

        SysUser sysUser = sysUserService.selectByName(username);
        sysUser = sysUser != null ? sysUser : new SysUser();
        String password = sysUser.getPassword();

        return new SimpleAuthenticationInfo(sysUser, password, EncryptionUtils.getSalt(username), this.getName());
    }
}
