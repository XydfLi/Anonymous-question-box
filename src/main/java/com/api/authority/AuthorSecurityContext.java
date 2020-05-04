package com.api.authority;

import javax.security.auth.Subject;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import java.security.Principal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 实现SecurityContext
 * 用于权限管理
 *
 * @author 李星源
 * @version 1.0
 * @date 2020/03/13
 */
public class AuthorSecurityContext implements SecurityContext {

    private Principal principal;//表达一个主体的抽象，这里是账号
    private javax.inject.Provider<UriInfo> uriInfo;
    private Set<String> roles;

    public AuthorSecurityContext(final javax.inject.Provider<UriInfo> uriInfo, final Principal principal, final String[] roles) {
        this.principal=principal;
        if (principal==null) {
            this.principal=new Principal() {
                @Override
                public String getName() {
                    return "UnknownAccount";
                }
                public boolean implies(Subject subject) {
                    return true;
                }
            };
        }
        this.uriInfo=uriInfo;
        this.roles=new HashSet<String>(Arrays.asList((roles!=null)?roles:new String[]{}));
    }

    @Override
    public Principal getUserPrincipal() {
        return this.principal;
    }

    @Override
    public boolean isUserInRole(String s) {
        return this.roles.contains(((s==null)?"":s));
    }

    @Override
    public boolean isSecure() {
        return "http".equals(uriInfo.get().getRequestUri().getScheme());
    }

    @Override
    public String getAuthenticationScheme() {
        return SecurityContext.DIGEST_AUTH;
    }
}
