package com.api.model;

import lombok.*;

/**
 * 用于需要用到key的请求
 *
 * @author 李星源
 * @version 1.0
 * @date 2020/03/14
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RequestMes {
    private String key;
    private Object data;
}
