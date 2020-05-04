package com.api.application;

import com.api.authority.AuthorPrincipal;
import com.api.authority.AuthorSecurityContext;
import com.api.crossDomain.CrossDomainRequestFilter;
import com.api.crossDomain.CrossDomainResponseFilter;
import com.api.dynamicBinding.JwtDynamicBinding;
import com.api.exception.ApplicationExceptionMapper;
import com.api.xss.XssFilter;
import lombok.extern.java.Log;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import javax.ws.rs.ApplicationPath;

/**
 * jersey框架注册类
 * 自动扫描资源类、注册组件
 *
 * @author 李星源
 * @version 1.0
 * @date 2020/03/11
 */
@ApplicationPath("/1.0/*")//应用的虚拟目录
@Log
public class RestApplication extends ResourceConfig {

    /**
     * 构造方法
     */
    public RestApplication(){
        this.packages("com.api");//扫描路径
        this.register(MultiPartFeature.class);//注册文件上传相关类
        this.register(ApplicationExceptionMapper.class);//注册异常相关类
        this.register(CrossDomainRequestFilter.class);//注册跨域请求过滤器
        this.register(CrossDomainResponseFilter.class);//注册跨域响应过滤器
        this.register(XssFilter.class);
        this.register(JwtDynamicBinding.class);//注册jwt动态绑定

        //注册权限管理相关类
        this.register(AuthorPrincipal.class);
        this.register(AuthorSecurityContext.class);
        this.register(RolesAllowedDynamicFeature.class);
    }
}
