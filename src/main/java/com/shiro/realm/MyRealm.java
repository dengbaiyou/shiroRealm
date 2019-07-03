package com.shiro.realm;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class MyRealm extends AuthorizingRealm {

    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    //身份认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {

        //获取用户名
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
        String username = usernamePasswordToken.getUsername();
        SimpleAuthenticationInfo info = null;
        Properties p = new Properties();
        try {
            if (path.startsWith("classpath:")){
                path = path.substring(10);
                p.load(MyRealm.class.getClassLoader().getResourceAsStream(path));
            }else if(path.startsWith("file:")){
                path = path.substring(5);
                p.load(new FileInputStream(path));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String password = p.getProperty("user." + username);
        if(password==null){
            throw new UnknownAccountException("未知的账户：username:"+username);
        }

        info = new SimpleAuthenticationInfo(username, password, this.getName());
        return info;
    }
}


