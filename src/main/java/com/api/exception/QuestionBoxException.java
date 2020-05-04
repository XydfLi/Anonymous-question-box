package com.api.exception;

/**
 * @author 李星源
 * @version 1.0
 * @date 2020/03/18
 */
public class QuestionBoxException extends ApplicationException {
    /**
     * 构造方法
     *
     * @param code  系统状态
     * @param cause Throwable对象
     */
    public QuestionBoxException(InfoCode code, Throwable cause) {
        super(code, cause);
    }
}
