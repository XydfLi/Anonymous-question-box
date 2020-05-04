package com.api.authority;

import com.api.exception.AuthorityException;
import com.api.exception.InfoCode;
import com.api.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.java.Log;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.UriInfo;

/**
 * 进行jwt验证、权限控制、单一登录
 *
 * @author 李星源
 * @version 1.0
 * @date 2020/03/13
 */
@Log
@Priority(Priorities.AUTHENTICATION+500)//优先级为第二级（1000~2000）
public class JwtSecurityRequestFilter implements ContainerRequestFilter {

    @Inject
    javax.inject.Provider<UriInfo> uriInfo;

    /**
     * 进行jwt验证、权限控制、单一登录
     *
     * @param containerRequestContext 请求信息
     */
    @Override
    public void filter(ContainerRequestContext containerRequestContext) {
        log.info("进入jwt请求过滤器");

        String jwt=containerRequestContext.getHeaderString("jwt");
        if ((!(jwt==""))&&(jwt!=null)){
            Claims claims=JwtUtil.parseJWT(jwt);
            JwtUtil.isLegal(claims);
            containerRequestContext.setSecurityContext(new AuthorSecurityContext(
                    uriInfo,
                    new AuthorPrincipal(claims.getSubject()),
                    new String[]{(String)claims.get("identity")}));
            log.info("通过jwt验证");
        } else {
            throw new AuthorityException(InfoCode.NO_LOGIN,new Throwable("jwt为空"));
        }
    }
}
