package com.api.exception;

/**
 * 用于抛出文件相关异常
 *
 * @author 李星源
 * @version 1.0
 * @date 2020/03/16
 */
public class FileException extends ApplicationException {
    /**
     * 构造方法
     *
     * @param code  系统状态
     * @param cause Throwable对象
     */
    public FileException(InfoCode code, Throwable cause) {
        super(code, cause);
    }
}
