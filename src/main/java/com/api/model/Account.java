package com.api.model;

import lombok.*;

/**
 * 数据库中的Account表
 *
 * @author 李星源
 * @version 1.0
 * @date 2020/03/12
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    private String accountName;//用户名
    private String password;//密码
    private int identity;//身份
    private String mailbox;//邮箱
    private String avatar;//头像地址
    private boolean questionBoxStatus;//提问箱状态（false为关闭，true为开启）
}
