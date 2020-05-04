package com.api.model;

import com.api.exception.InfoCode;
import lombok.*;

import javax.servlet.http.HttpServletResponse;

/**
 * 统一响应类
 *
 * @author 李星源
 * @version 1.0
 * @date 2020/03/11
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ResponseMes {
    private int status;//状态码
    private int infoCode;//系统状态码
    private String info;//系统状态信息
    private Object data;//返回的数据信息

    /**
     * 前台请求操作成功时运行该方法
     *
     * @param data 需要返回给前台的数据
     * @return ResponseMes对象
     */
    public static ResponseMes success(Object data){
        return new ResponseMes(HttpServletResponse.SC_OK,
                InfoCode.OK.getCode(),
                InfoCode.OK.getValue(),
                data);
    }

    /**
     * 前台请求操作失败时运行该方法
     *
     * @param status 需要返回给前台的状态码
     * @param infoCode 需要返回给前台的系统状态码
     * @param info 需要返回给前天的系统状态信息
     * @param data 需要返回给前台的数据
     * @return ResponseMes对象
     */
    public static ResponseMes failure(int status,int infoCode,String info,Object data){
        return new ResponseMes(status,
                infoCode,
                info,
                data);
    }
}
