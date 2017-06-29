package club.calabashbrothers.zkadmin.shiro.realm;


import club.calabashbrothers.zkadmin.domain.entity.User;
import club.calabashbrothers.zkadmin.service.UserService;
import club.calabashbrothers.zkadmin.shiro.bind.ShiroUser;
import club.calabashbrothers.zkadmin.utils.RegexpUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.AllowAllCredentialsMatcher;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        ShiroUser shiroUser = (ShiroUser)principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        /*User user = userService.findUserById(shiroUser.getUserId());
        if(StringUtils.isNotEmpty(user.getRoleIds())){
            Long [] roleIds = (Long[]) ConvertUtils.convert(user.getRoleIds().split(","),Long.class);
            authorizationInfo.setRoles(userService.findRoles(roleIds));
            authorizationInfo.setStringPermissions(userService.findPermissions(roleIds));
            if(authorizationInfo.getRoles().contains(SysRole.ADMIN)){ //管理员 赋予全部权限
                authorizationInfo.getStringPermissions().add("*");
            }
            if(authorizationInfo.getRoles().contains(SysRole.READONLY)){ //管理员 赋予全部权限
                authorizationInfo.getStringPermissions().add("*:read");
                authorizationInfo.getStringPermissions().add("*:view");
            }
        }
        //修改最后登录的时间
        user.setLastLoginTime(new Date());
        userService.updateUser(user);*/
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {


        String username = (String)token.getPrincipal();



        User user = null;
        if(RegexpUtils.isMobilephone(username)){
            user = userService.findUserByPhone(username);
        }else {
            user = userService.findUserByLoginName(username);
        }

        if(user == null) {
            throw new UnknownAccountException();//没找到帐号
        }


        UsernamePasswordToken hostToken = (UsernamePasswordToken)token;

        //交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得不好可以自定义实现
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                new ShiroUser(user.getId(),user.getLoginName(),user.getNickname()), //用户名
                user.getPassword(), //密码
                ByteSource.Util.bytes(user.getCredentialsSalt()),//salt=username+salt
                getName()  //realm name
        );
        return authenticationInfo;
    }

    @Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }

    @Override
    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        super.clearCachedAuthenticationInfo(principals);
    }

    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }

    public void clearAllCachedAuthorizationInfo() {
        getAuthorizationCache().clear();
    }

    public void clearAllCachedAuthenticationInfo() {
        getAuthenticationCache().clear();
    }

    public void clearAllCache() {
        clearAllCachedAuthenticationInfo();
        clearAllCachedAuthorizationInfo();
    }

    @Override
    protected void assertCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) throws AuthenticationException {
        CredentialsMatcher cm = getCredentialsMatcher();
        if (cm != null) {
            if (!cm.doCredentialsMatch(token, info)) {
                //not successful - throw an exception to indicate this:
                String msg = "Submitted credentials for token [" + token + "] did not match the expected credentials.";
                throw new IncorrectCredentialsException(msg);
            }else {
                //记录登陆日志
/*                ShiroUser shiroUser = (ShiroUser)(info.getPrincipals().getPrimaryPrincipal());
                Log log = LogBuilder.OP_LOG.buildCommonLog("登陆", shiroUser.getClientIp(), shiroUser.getLoginName());
                logService.log(log);*/
            }
        } else {
            throw new AuthenticationException("A CredentialsMatcher must be configured in order to verify " +
                    "credentials during authentication.  If you do not wish for credentials to be examined, you " +
                    "can configure an " + AllowAllCredentialsMatcher.class.getName() + " instance.");
        }
    }


}
