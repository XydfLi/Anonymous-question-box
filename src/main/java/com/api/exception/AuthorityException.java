package com.api.exception;

/**
 * 与权限有关的异常
 *
 * @author 李星源
 * @version 1.0
 * @date 2020/03/13
 */
public class AuthorityException extends ApplicationException {

    /**
     * 构造方法
     *
     * @param code  系统状态
     * @param cause Throwable对象
     */
    public AuthorityException(InfoCode code, Throwable cause) {
        super(code, cause);
    }
}
