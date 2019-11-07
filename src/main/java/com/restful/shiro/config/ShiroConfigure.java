package com.restful.shiro.config;

import com.restful.shiro.filter.MyAuthenticationFilter;
import com.restful.shiro.util.EncryptionUtils;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.AuthenticationStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.Filter;
import java.util.*;

/**
 * @author LiShuLin
 * @version v1.0
 * @program restful_boot
 * @description shiro 配置
 * @date 2019-09-19 15:33
 */
@Configuration
public class ShiroConfigure {

    @Bean
    @DependsOn(value = {"authenticator", "ehCacheManager"})
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager dsm = new DefaultWebSecurityManager();
        dsm.setCacheManager(ehCacheManager());
        dsm.setRememberMeManager(rememberMeManager());
        dsm.setAuthenticator(authenticator());
        //弃用 持久化 session
        /*dsm.setSessionManager(sessionManager());*/
        Collection<Realm> realms = new ArrayList<>(8);
        realms.add(shiroRealmCustomize());
        dsm.setRealms(realms);
        return dsm;
    }

    /**
     * 自定义认证
     */
    @Bean
    public ShiroRealm shiroRealmCustomize() {
        ShiroRealm shiroRealm = new ShiroRealm();
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        credentialsMatcher.setHashAlgorithmName(EncryptionUtils.HASH_ALGORITHM_NAME);
        credentialsMatcher.setHashIterations(EncryptionUtils.HASH_ITERATIONS);
        shiroRealm.setCredentialsMatcher(credentialsMatcher);
        return shiroRealm;
    }

    /**
     * sessionId 生成器
     */
    @Bean
    @Deprecated
    public JavaUuidSessionIdGenerator sessionIdGenerator() {
        return new JavaUuidSessionIdGenerator();
    }

    /**
     * 注入自己编写的 session 持久化
     */
    @Bean
    @DependsOn(value = {"sessionIdGenerator"})
    @Deprecated
    public SessionPermanentClass sessionDAO() {
        SessionPermanentClass sessionPermanentClass = new SessionPermanentClass();
        sessionPermanentClass.setSessionIdGenerator(sessionIdGenerator());
        sessionPermanentClass.setActiveSessionsCacheName("shiro-activeSessionCache");
        return sessionPermanentClass;
    }

    /**
     * 会话管理
     */
    @Bean
    @DependsOn(value = {"sessionDAO"})
    @Deprecated
    public DefaultSessionManager sessionManager() {
        DefaultSessionManager manager = new DefaultWebSessionManager();
        manager.setGlobalSessionTimeout(1000 * 60 * 30);
        manager.setSessionValidationSchedulerEnabled(true);
        manager.setDeleteInvalidSessions(true);
        manager.setSessionDAO(sessionDAO());
        return manager;
    }

    /**
     * 认证器
     */
    @Bean
    public ModularRealmAuthenticator authenticator() {
        ModularRealmAuthenticator mra = new ModularRealmAuthenticator();
        AuthenticationStrategy as = new AtLeastOneSuccessfulStrategy();
        mra.setAuthenticationStrategy(as);
        return mra;
    }

    /**
     * 方法描述: 缓存配置
     *
     * @return org.apache.shiro.cache.ehcache.EhCacheManager
     * @author LiShuLin
     * @date 2019/9/19
     */
    @Bean
    public EhCacheManager ehCacheManager() {
        EhCacheManager ehCacheManager = new EhCacheManager();
        ehCacheManager.setCacheManagerConfigFile("classpath:ehcache/ehcache.xml");
        return ehCacheManager;
    }

    /**
     * cookie 配置
     */
    @Bean
    public SimpleCookie rememberMeCookie() {
        SimpleCookie simpleCookie = new SimpleCookie();
        simpleCookie.setName("rememberMe");
        simpleCookie.setHttpOnly(true);
        simpleCookie.setMaxAge(60);
        return simpleCookie;
    }

    /**
     * 注入 rememberMeCookie 配置
     */
    @Bean
    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        byte[] bytes = Base64.decode("4AvVhmFLUs0KTA3Kprsdag==");
        cookieRememberMeManager.setCipherKey(bytes);
        cookieRememberMeManager.setCookie(rememberMeCookie());
        return cookieRememberMeManager;
    }

    /**
     * shiro bean 生命周期处理器
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 在 ioc 中使用 shiro 注释
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        return new DefaultAdvisorAutoProxyCreator();
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor sourceAdvisor() {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager());
        return advisor;
    }

    @Bean
    @DependsOn(value = {"securityManager"})
    public ShiroFilterFactoryBean shiroFilterFactoryBean() {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager());

        Map<String, Filter> filterMap = new HashMap<>(8);
        filterMap.put("auth", new MyAuthenticationFilter());
        shiroFilterFactoryBean.setFilters(filterMap);

        LinkedHashMap<String, String> hashMap = new LinkedHashMap<>();
        hashMap.put("/login", "anon");
        hashMap.put("/api/**", "anon");
//        hashMap.put("/**", "auth,authc");
        hashMap.put("/**", "anon");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(hashMap);
        return shiroFilterFactoryBean;
    }

    @Bean
    @DependsOn(value = {"shiroFilterFactoryBean"})
    public DelegatingFilterProxy filterProxy() {
        DelegatingFilterProxy filterProxy = new DelegatingFilterProxy();
        filterProxy.setTargetBeanName("shiroFilterFactoryBean");
        return filterProxy;
    }


}
