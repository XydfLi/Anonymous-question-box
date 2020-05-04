package com.api.model;

import lombok.*;

/**
 * @author 李星源
 * @version 1.0
 * @date 2020/03/14
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class IPs {
    private String accountName;//用户名
    private String IP;//登入ip
    private int count;//登入次数
}
