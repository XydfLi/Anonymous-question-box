package com.api.crossDomain;

import lombok.extern.java.Log;

import javax.annotation.Priority;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;

/**
 * @author 李星源
 * @version 1.0
 * @date 2020/03/12
 */
@Log
@Priority(Priorities.AUTHENTICATION-500)//优先级为第一级（<1000）
public class CrossDomainResponseFilter implements ContainerResponseFilter {

    /**
     * 添加允许的权限
     *
     * @param containerRequestContext 包含请求相关的内容（不能修改）
     * @param containerResponseContext 包含响应相关的内容
     */
    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) {
        log.info("进入跨域响应过滤器");

        //允许跨域请求
        if("OPTIONS".equalsIgnoreCase(containerRequestContext.getMethod()))
            containerResponseContext.setStatus(HttpServletResponse.SC_OK);
        containerResponseContext.getHeaders().add("Access-Control-Expose-Headers","jwt,authorization");
        containerResponseContext.getHeaders().add("Access-Control-Allow-Origin",containerRequestContext.getHeaderString("origin"));
        containerResponseContext.getHeaders().add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization,jwt");
        containerResponseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");
        containerResponseContext.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD, PATCH");
        containerResponseContext.getHeaders().add("Access-Control-Max-Age", "1209600");//14天
    }
}
