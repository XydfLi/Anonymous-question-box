package com.api.jwt;

import lombok.*;

import java.util.Date;

/**
 * 定义jwt所携带的内容
 *
 * @author 李星源
 * @version 1.0
 * @date 2020/03/13
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class JwtUser {
    private String accountName;//用户名
    private String identity;//身份
    private String mailbox;//邮箱
    private Date loginTime;//记录登入时间，实现单一登录，可以将非法者踢下线
}
