package com.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.java.Log;

/**
 * 用户身份的枚举
 * TODO : controller层中的@RolesAllowed需要手动输入
 *
 * @author 李星源
 * @version 1.0
 * @date 2020/03/13
 */
@Getter
@AllArgsConstructor
@Log
public enum Identity {
    ADMINISTRATORS(1,"admin"),//管理员
    ORDINARY(2,"ordi"),//普通用户
    UNACTIVATED(3,"unact"),//未激活用户
    BANNED(4,"ban"),//被封禁用户
    UNKNOWN(5,"unknown");//未知用户

    private int code;
    public String value;

    public static Identity getIdentity(int code){
        switch (code){
            case 1:
                return ADMINISTRATORS;
            case 2:
                return ORDINARY;
            case 3:
                return UNACTIVATED;
            case 4:
                return BANNED;
            case 5:
                return UNKNOWN;
        }
        return null;
    }

}
