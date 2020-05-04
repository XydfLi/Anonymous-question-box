package com.api.authority;

import java.security.Principal;

/**
 * 实现身份接口，用于权限管理
 *
 * @author 李星源
 * @version 1.0
 * @date 2020/03/13
 */
public class AuthorPrincipal implements Principal {
    private String name;

    AuthorPrincipal(String name){
        this.name=name;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
