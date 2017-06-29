package club.calabashbrothers.zkadmin.config;

import club.calabashbrothers.zkadmin.shiro.Constants;
import club.calabashbrothers.zkadmin.shiro.credentials.RetryLimitHashedCredentialsMatcher;
import club.calabashbrothers.zkadmin.shiro.filter.ForceLogoutFilter;
import club.calabashbrothers.zkadmin.shiro.filter.LoginFilter;
import club.calabashbrothers.zkadmin.shiro.realm.UserRealm;
import club.calabashbrothers.zkadmin.shiro.spring.SpringCacheManagerWrapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.ServletContainerSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by liaojiacan on 2016/11/11.
 */
@Configuration
@AutoConfigureAfter(CommonConfig.class)
public class ShiroConfiguration {

    @Autowired
    private CacheManager springCacheManager;

    private static Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();

    @Bean(name="cacheManager")
    public SpringCacheManagerWrapper cacheManager(){
        return new SpringCacheManagerWrapper(){{setCacheManager(springCacheManager);}};
    }

    @Bean(name="credentialsMatcher")
    public CredentialsMatcher credentialsMatcher(){
        RetryLimitHashedCredentialsMatcher credentialsMatcher = new RetryLimitHashedCredentialsMatcher(cacheManager());
        credentialsMatcher.setHashAlgorithmName("md5");
        credentialsMatcher.setHashIterations(2);
        credentialsMatcher.setStoredCredentialsHexEncoded(true);
        return credentialsMatcher;
    }

    @Bean(name = "userRealm")
    public UserRealm userRealm(){
        UserRealm userRealm = new UserRealm();
        userRealm.setAuthorizationCacheName("authorizationCache");
        userRealm.setCacheManager(cacheManager());
        userRealm.setCredentialsMatcher(credentialsMatcher());
        userRealm.setCachingEnabled(true);
        return userRealm;
    }



    @Bean(name="sessionIdGenerator")
    public SessionIdGenerator sessionIdGenerator(){
        return new JavaUuidSessionIdGenerator();
    }

    @Bean(name="sessionIdCookie")
    public SimpleCookie sessionIdCookie (){
        SimpleCookie simpleCookie = new SimpleCookie("sid");
        simpleCookie.setHttpOnly(true);
        simpleCookie.setMaxAge(-1);
        return simpleCookie;
    }

    @Bean(name="rememberMeCookie")
    public SimpleCookie rememberMeCookie(){
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        simpleCookie.setHttpOnly(true);
        simpleCookie.setMaxAge(2592000); //30天
        return simpleCookie;
    }

    @Bean(name="rememberMeManager")
    public CookieRememberMeManager rememberMeManager(){
        CookieRememberMeManager rememberMeManager = new CookieRememberMeManager();
        rememberMeManager.setCookie(rememberMeCookie());
        rememberMeManager.setCipherKey(Base64.decode(Constants.CipherKey_REMEMBERME));
        return rememberMeManager;
    }


    @Bean(name = "sessionManager")
    public ServletContainerSessionManager sessionManager(){
       return new ServletContainerSessionManager();
    }

    @Bean(name = "securityManager")
    public DefaultWebSecurityManager securityManager(){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setSessionManager(sessionManager());
        securityManager.setRealm(userRealm());
        securityManager.setCacheManager(cacheManager());
        securityManager.setRememberMeManager(rememberMeManager());
        SecurityUtils.setSecurityManager(securityManager);
        return securityManager;
    }




    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean getShiroFilterFactoryBean() {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager());
        shiroFilterFactoryBean.setLoginUrl("/login.html");
        shiroFilterFactoryBean.setSuccessUrl("/overview/dashboard.html");
        Map<String, Filter> filters = new LinkedHashMap<String, Filter>();
        filters.put("authc", createLoginFilter());
        filters.put("forceLogout", new ForceLogoutFilter());
        shiroFilterFactoryBean.setFilters(filters);
        filterChainDefinitionMap.put("/static/**", "anon");
        filterChainDefinitionMap.put("/test/**", "anon");
        filterChainDefinitionMap.put("/logout", "logout");
        filterChainDefinitionMap.put("/authenticated", "authc");
        filterChainDefinitionMap.put("/login.json", "authc");
        filterChainDefinitionMap.put("/**", "user");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }


    public LoginFilter createLoginFilter(){
        LoginFilter loginFilter = new LoginFilter();
        loginFilter.setLoginUrl("/login.html");
        loginFilter.setSuccessUrl("/overview/dashboard.html");
        loginFilter.setUsernameParam("username");
        loginFilter.setPasswordParam("password");
        loginFilter.setRememberMeParam("rememberMe");
        return  loginFilter;
    }

    @Bean(name = "lifecycleBeanPostProcessor")
    public static LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }


    public static void main(String[] args) {
        System.out.println(Base64.decode("GDjptpJ1tHfK0lZKH=s2o").length);
    }
}
