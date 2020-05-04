package com.api.xss;

import lombok.extern.java.Log;
import org.apache.commons.io.IOUtils;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URI;
import java.nio.charset.StandardCharsets;

/**
 * 防止XSS攻击
 *
 * @author 李星源
 * @version 1.0
 * @date 2020/03/13
 */
@Log
@PreMatching
@Priority(Priorities.AUTHENTICATION-500)//优先级为第一级（<1000）
public class XssFilter implements ContainerRequestFilter {

    /**
     * 过滤特殊字符
     *
     * @param containerRequestContext 请求信息
     */
    @Override
    public void filter(ContainerRequestContext containerRequestContext) {
        log.info("进入XSS请求过滤器");
        try {
            //过滤URI
            URI uri=containerRequestContext.getUriInfo().getRequestUri();
            String newUri=uri.getScheme()+"://"+uri.getAuthority();
            String que=uri.getQuery();
            String path=uri.getPath();
            path=cleanXSS(path);//过滤路径
            if (que==null||que==""){
                newUri=newUri+path;
            } else{
                que=cleanXSS(que);//过滤参数
                newUri=newUri+path+"?"+que;
            }
            containerRequestContext.setRequestUri(uri.resolve(newUri));

            if (!path.substring(path.length()-6).equals("avatar")){//上传头像有另外的过滤
                //过滤请求实体
                InputStream orin=containerRequestContext.getEntityStream();
                StringWriter writer=new StringWriter();
                IOUtils.copy(orin,writer,StandardCharsets.UTF_8.name());
                String str=writer.toString();
                str=cleanXSS(str);//过滤
                InputStream targetStream=IOUtils.toInputStream(str,StandardCharsets.UTF_8.name());
                containerRequestContext.setEntityStream(targetStream);
            }
        } catch (IOException e){
            e.printStackTrace();
        }

    }

    /**
     * 过滤字符串
     *
     * @param value 待过滤字符串
     * @return 过滤后的字符串
     */
    private String cleanXSS(String value) {
        value=value.replaceAll("<", "& lt;").replaceAll(">", "& gt;");
        value=value.replaceAll("\\(", "& #40;").replaceAll("\\)", "& #41;");
        value=value.replaceAll("'", "& #39;");
        value=value.replaceAll("eval\\((.*)\\)", "");
        value=value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
        value=value.replaceAll("script", "");
        value=value.replace(" ","");
        return value;
    }
}
