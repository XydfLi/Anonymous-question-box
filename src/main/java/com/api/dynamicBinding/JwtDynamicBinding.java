package com.api.dynamicBinding;

import com.api.authority.JwtSecurityRequestFilter;
import com.api.crossDomain.CrossDomainRequestFilter;
import com.api.crossDomain.CrossDomainResponseFilter;

import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;

/**
 * jwt动态绑定
 *
 * @author 李星源
 * @version 1.0
 * @date 2020/03/13
 */
public class JwtDynamicBinding implements DynamicFeature {

    /**
     * 登入、注册、获取公钥、发送链接、用户修改密码链接、用户激活邮箱链接、获取图形验证码不需要验证jwt
     *
     * @param resourceInfo 为了获得方法名称
     * @param featureContext 为了实现注册
     */
    @Override
    public void configure(ResourceInfo resourceInfo, FeatureContext featureContext) {
        String methodName=resourceInfo.getResourceMethod().getName();

        if (!methodName.equals("login")&&
                !methodName.equals("register")&&
                !methodName.equals("getKey")&&
                !methodName.equals("passwordUri")&&
                !methodName.equals("updatePassword")&&
                !methodName.equals("actEmail")&&
                !methodName.equals("getCode")&&
                !methodName.equals("getAvatar"))
            featureContext.register(JwtSecurityRequestFilter.class);
    }

}
