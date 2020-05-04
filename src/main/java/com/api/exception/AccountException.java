package com.api.exception;

/**
 * Account相关异常
 *
 * @author 李星源
 * @version 1.0
 * @date 2020/03/14
 */
public class AccountException extends ApplicationException {
    /**
     * 构造方法
     *
     * @param code  系统状态
     * @param cause Throwable对象
     */
    public AccountException(InfoCode code, Throwable cause) {
        super(code, cause);
    }
}
