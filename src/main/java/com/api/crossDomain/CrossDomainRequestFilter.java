package com.api.crossDomain;

import lombok.extern.java.Log;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;

/**
 * @author 李星源
 * @version 1.0
 * @date 2020/03/12
 */
@Log
@Priority(Priorities.AUTHENTICATION-500)//优先级为第一级（<1000）
public class CrossDomainRequestFilter implements ContainerRequestFilter {

    /**
     * 添加允许的权限
     *
     * @param containerRequestContext 包含请求相关的内容
     */
    @Override
    public void filter(ContainerRequestContext containerRequestContext) {
        log.info("进入跨域请求过滤器");
        //允许跨域请求
        containerRequestContext.getHeaders().add("Access-Control-Expose-Headers","jwt, authorization");
        containerRequestContext.getHeaders().add("Access-Control-Allow-Origin",containerRequestContext.getHeaderString("origin"));
        containerRequestContext.getHeaders().add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization,jwt");
        containerRequestContext.getHeaders().add("Access-Control-Allow-Credentials", "true");
        containerRequestContext.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD, PATCH");
        containerRequestContext.getHeaders().add("Access-Control-Max-Age","1209600");//14天
    }
}

//AUTHENTICATION = 1000身份验证
//AUTHORIZATION = 2000权限
//HEADER_DECORATOR = 3000头修饰
//ENTITY_CODER = 4000实体编码器
//USER = 5000用户