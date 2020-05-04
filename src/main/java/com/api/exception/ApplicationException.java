package com.api.exception;

import lombok.Getter;
import lombok.extern.java.Log;

/**
 * 自定义异常基类
 * 继承RuntimeException
 *
 * @author 李星源
 * @version 1.0
 * @date 2020/03/11
 */
@Getter
@Log
public class ApplicationException extends RuntimeException {

    private InfoCode code=InfoCode.OK;

    /**
     * 构造方法
     *
     * @param code 系统状态
     * @param cause Throwable对象
     */
    public ApplicationException(InfoCode code,Throwable cause) {
        super(code.getValue(),cause);
        this.code = code;
    }
}
